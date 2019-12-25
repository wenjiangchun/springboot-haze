package com.haze.vsail.bus.web.controller;

import com.haze.system.entity.Group;
import com.haze.system.service.GroupService;
import com.haze.system.utils.Status;
import com.haze.vsail.bus.entity.Bus;
import com.haze.vsail.bus.entity.BusModel;
import com.haze.vsail.bus.service.BusModelService;
import com.haze.vsail.bus.service.BusService;
import com.haze.vsail.bus.util.BusEventType;
import com.haze.web.BaseCrudController;
import com.haze.web.utils.WebMessage;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/v/bus")
public class BusController extends BaseCrudController<Bus, Long> {

    private BusModelService busModelService;

    private BusService busService;

    private GroupService groupService;

    public BusController(BusModelService busModelService, BusService busService, GroupService groupService) {
        super("vsail", "bus", "车辆", busService);
        this.busModelService = busModelService;
        this.busService = busService;
        this.groupService = groupService;
    }

    /**
     * 处理日期字符串
     * @param binder 参数绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
        List<BusModel> list = this.busModelService.findAll();
        model.addAttribute("modelList", list);
        String parentId = request.getParameter("parentId");
        if (parentId != null) {
            Group parent = this.groupService.findById(Long.parseLong(parentId));
            model.addAttribute("parent", parent);
            model.addAttribute("parentId", parentId);
            model.addAttribute("root", parent.getRootGroup());
        }
        //查询未投入运营的车辆信息
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("used", false);
        queryParams.put("deleted", false);
        List<Bus> unUsedBusList = this.busService.findAll(queryParams);
        model.addAttribute("unUsedList", unUsedBusList);
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
        if (queryVariables.get("parent.id") != null) {
            //查询父节点
            Group group = groupService.findById(Long.valueOf(queryVariables.get("parent.id").toString()));
            List<Group> groupList = group.getChildList(Status.ENABLE);
            groupList.add(group);
            queryVariables.put("lineGroup_in", groupList);
            //后续接入更多公交公司,底层数据库会根据不同公交公司进行表分区,分区后增加该查询条件后会优化查询
            //queryVariables.put("rootGroup", group.getRootGroup());
            queryVariables.remove("parent.id");
        }
        if (queryVariables.get("used") != null) {
            queryVariables.put("used", Boolean.valueOf(queryVariables.get("used").toString()));
        }
    }

    /**
     * 获取所有运营公司信息
     *
     * @return
     */
    @RequestMapping(value = "getBusGroups")
    @ResponseBody
    public List<Group> getTopGroups() {
        //查询运营公司信息
        List<Group> groups = busService.getBusGroups();
        Set<Group> newGroup = new HashSet<>();
        Group root = new Group();
        root.setFullName("运营公司");
        for (Group g : groups) {
            g.setUsers(null);
            g.setChilds(null);
            //g.setGroupType(g.getGroupType().getCode());
            if (g.getPid() == null) {
                g.setParent(root);
            }
            newGroup.add(g);
        }
        newGroup.add(root);
        return new ArrayList<>(newGroup);
    }

    @GetMapping("info")
    public String info(Model model) {
        return "vsail/busInfo/list";
    }

    @GetMapping("addInfo")
    public String addInfo(Model model) {
        return "vsail/busInfo/add";
    }

    @GetMapping("editInfo/{id}")
    public String editInfo(Model model, @PathVariable Long id) {
        Bus bus = this.busService.findById(id);
        model.addAttribute("bus", bus);
        return "vsail/busInfo/edit";
    }

    @PostMapping(value = "saveBusInfo")
    @ResponseBody
    public WebMessage saveBusInfo(Bus bus, HttpServletRequest request) {
        try {
            //判断车辆是否已存在
            if (!bus.isNew()) {
                Bus existBus = this.busService.findById(bus.getId());
                existBus.setVin(bus.getVin());
                existBus.setBusNum(bus.getBusNum());
                existBus.setModelName(bus.getModelName());
                existBus.setAssembleAddress(bus.getAssembleAddress());
                existBus.setAssembleDay(bus.getAssembleDay());
                existBus.setFactoryName(bus.getFactoryName());
                if (bus.getUsed()) {
                    //如果车辆已在运营中 则保存运营信息
                    this.busService.saveBus(existBus, BusEventType.BUS_EVENT_UPDATE);
                } else {
                    this.busService.saveBusInfo(existBus);
                }
            } else {
                this.busService.saveBusInfo(bus);
            }
            logger.debug("保存/更新车辆基本信息成功,{}", bus);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.error("保存/更新车辆基本信息失败,{},error={}", bus, e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }


    /**
     * 保存运营信息
     * @param bus 车辆信息
     * @param request
     * @return
     */
    @PostMapping(value = "saveBus")
    @ResponseBody
    public WebMessage saveBus(Bus bus, int eventCode, HttpServletRequest request) {
        try {
            //判断车辆是否运营
            if (!bus.isNew()) {
                Bus existBus = this.busService.findById(bus.getId());
                existBus.setUsed(true);
                existBus.setDrivingNum(bus.getDrivingNum());
                Group g = groupService.findById(bus.getLineGroup().getId());
                //获取场站信息
                Group siteGroup = g.getParent();
                //获取分公司信息
                Group branchGroup = siteGroup.getParent();
                //获取运营公司信息
                Group rootGroup = branchGroup.getParent();
                existBus.setLineGroup(g);
                existBus.setSiteGroup(siteGroup);
                existBus.setBranchGroup(branchGroup);
                existBus.setRootGroup(rootGroup);
                existBus.setProductNum(bus.getProductNum());
                existBus.setProductBrand(bus.getProductBrand());
                existBus.setProductType(bus.getProductType());
                this.busService.saveBus(existBus, BusEventType.parser(eventCode));
            }
            logger.debug("保存/更新车辆运营信息成功,{}", bus);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.error("保存/更新车辆运营信息失败,{},error={}", bus, e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }
}
