package com.haze.vsail.stat.web.controller;

import com.haze.shiro.ShiroUser;
import com.haze.shiro.util.ShiroUtils;
import com.haze.system.entity.Group;
import com.haze.system.service.DictService;
import com.haze.system.service.GroupService;
import com.haze.vsail.bus.service.BusService;
import com.haze.vsail.bus.util.VsailConstants;
import com.haze.vsail.stat.entity.BusBreakDownLog;
import com.haze.vsail.stat.entity.BusFireLog;
import com.haze.vsail.stat.entity.BusOnOffLog;
import com.haze.vsail.stat.service.VsailStatService;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/v/stat")
public class VsailStatController {

    private VsailStatService vsailStatService;

    private BusService busService;
    private GroupService groupService;

    public VsailStatController(VsailStatService vsailStatService, BusService busService) {
        this.vsailStatService = vsailStatService;
        this.busService = busService;
    }

    @GetMapping("/onoff")
    public String viewBusOnOffLog(Model model) {
        return "vsail/stat/onoff";
    }

    @PostMapping(value = "searchOnOff")
    @ResponseBody
    public DataTablePage search(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        Page<BusOnOffLog> list = this.vsailStatService.findOnOffPage(p, dataTableParams.getQueryVairables());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }

    @GetMapping("/fire")
    public String viewBusFireLog(Model model) {
        return "vsail/stat/fire";
    }

    @PostMapping(value = "searchFire")
    @ResponseBody
    public DataTablePage searchFireLog(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        Page<BusFireLog> list = this.vsailStatService.findFirePage(p, dataTableParams.getQueryVairables());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }

    @GetMapping("/breakdown")
    public String viewBusBreakDownLog(Model model) {
        return "vsail/stat/breakdown";
    }

    @PostMapping(value = "searchBreakdown")
    @ResponseBody
    public DataTablePage searchBreakdownLog(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        Page<BusBreakDownLog> list = this.vsailStatService.findBreakDownPage(p, dataTableParams.getQueryVairables());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }

    @GetMapping("/chart")
    public String viewChart(Model model) {
        //获取用户信息
        ShiroUser shiroUser = ShiroUtils.getCurrentUser();
        Objects.requireNonNull(shiroUser);
        List<Group> groupList = new ArrayList<>();
        if (shiroUser.isSuperAdmin()) {
            groupList = busService.getBusGroups().stream().filter(group -> group.getParent() == null).collect(Collectors.toList());
        } else {
            //根据用户机构类型判断
            Group rootGroup = shiroUser.getGroup().getRootGroup();
            if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_VSAIL)) { //vsail机构下用户返回所有车辆
                groupList = busService.getBusGroups().stream().filter(group -> group.getParent() == null).collect(Collectors.toList());
            } else if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_BUS)) {
               //其它机构用户返回本机构信息
                groupList.add(rootGroup);
            } else {
                //TODO 暂不处理
            }
        }
        model.addAttribute("groupList", groupList);
        return "vsail/stat/chart";
    }


    @PostMapping(value = "getStateCount")
    @ResponseBody
    public Map<String, Object> getStateCount(Date startDay, Date endDay) {
        return null;
    }
}
