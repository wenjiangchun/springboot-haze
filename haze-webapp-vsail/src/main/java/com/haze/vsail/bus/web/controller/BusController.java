package com.haze.vsail.bus.web.controller;

import com.haze.system.entity.Group;
import com.haze.system.service.GroupService;
import com.haze.system.utils.Status;
import com.haze.vsail.bus.entity.Bus;
import com.haze.vsail.bus.entity.BusModel;
import com.haze.vsail.bus.service.BusModelService;
import com.haze.vsail.bus.service.BusService;
import com.haze.web.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/v/bus")
public class BusController extends BaseCrudController<Bus, Long> {

    private BusModelService busModelService;

    private BusService busService;

    private GroupService groupService;

    public BusController(BusModelService busModelService, BusService busService, GroupService groupService) {
        super("vsail", "bus", "产品", busService);
        this.busModelService = busModelService;
        this.busService = busService;
        this.groupService = groupService;
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
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
        if (queryVariables.get("parent.id") != null) {
            //查询父节点
            Group group = groupService.findById(Long.valueOf(queryVariables.get("parent.id").toString()));
            List<Group> groupList = group.getChildList(Status.ENABLE);
            groupList.add(group);
            queryVariables.put("group_in", groupList);
            //后续接入更多公交公司,底层数据库会根据不同公交公司进行表分区,分区后增加该查询条件后会优化查询
            queryVariables.put("rootGroup", group.getRootGroup());
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
            g.setGroupType(null);
            if (g.getPid() == null) {
                g.setParent(root);
            }
            newGroup.add(g);
        }
        newGroup.add(root);
        return new ArrayList<>(newGroup);
    }

}
