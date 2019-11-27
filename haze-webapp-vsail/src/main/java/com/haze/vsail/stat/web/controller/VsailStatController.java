package com.haze.vsail.stat.web.controller;

import com.alibaba.excel.EasyExcel;
import com.haze.common.util.HazeDateUtils;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/v/stat")
public class VsailStatController {

    private VsailStatService vsailStatService;

    private BusService busService;

    public VsailStatController(VsailStatService vsailStatService, BusService busService) {
        this.vsailStatService = vsailStatService;
        this.busService = busService;
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
        //设置开始日期和结束日期
        model.addAttribute("startDay", HazeDateUtils.format(HazeDateUtils.addDays(new Date(), -7), "yyyy-MM-dd"));
        model.addAttribute("endDay", HazeDateUtils.format(new Date(), "yyyy-MM-dd"));
        return "vsail/stat/chart";
    }


    @PostMapping(value = "getStatCount")
    @ResponseBody
    public Map<String, List<Object[]>> getStatCount(Long rootGroupId, Date startDay, Date endDay) {
        //计算开始日期和结束日期
        if (startDay == null) {
            startDay = HazeDateUtils.fromLocalDate(LocalDate.ofYearDay(1970, 1));
        }
        if (endDay == null) {
            endDay = HazeDateUtils.fromLocalDate(LocalDate.now());;
        }
        //不同车型火警数量
        List<Object[]> modelFireCount = vsailStatService.getFireCountByBusModel(rootGroupId, startDay,endDay);
        //不同车型故障数量
        List<Object[]> modelbdCount = vsailStatService.getBreakDownCountByBusModel(rootGroupId, startDay,endDay);
        //不同线路火警数量
        List<Object[]> groupFireCount = vsailStatService.getFireCountByGroup(rootGroupId, startDay,endDay);
        //不同线路故障数量
        List<Object[]> groupbdCount = vsailStatService.getBreakDownCountByGroup(rootGroupId, startDay,endDay);
        Map<String, List<Object[]>> result = new HashMap<>();
        //模拟数据 TODO 后续删除该代码
        String[] model_name = {"model1", "model2"};
        String[] groupName = {"906线路", "315线路"};
        for (int i = 0; i < 100; i++) {
            int year = 2019;
            int month = new Random().nextInt(12);
            if (month == 0) month = 1;
            int day = new Random().nextInt(30);
            if (day == 0) day = 1;
            for (String s : model_name) {
                Object[] rs = new Object[]{s, year, month, day, new Random().nextInt(20)};
                modelFireCount.add(rs);
                modelbdCount.add(rs);
            }
            for (String s : groupName) {
                Object[] rs = new Object[]{s, year, month, day, new Random().nextInt(20)};
                groupFireCount.add(rs);
                groupbdCount.add(rs);
            }
        }
        result.put("modelFireCount", modelFireCount);
        result.put("modelbdCount", modelbdCount);
        result.put("groupFireCount", groupFireCount);
        result.put("groupbdCount", groupbdCount);

        List<Object[]> fireCount = vsailStatService.getFireCount(rootGroupId, startDay,endDay);
        List<Object[]> statDays = new ArrayList<>();
        //计算共有多少天
        int day = HazeDateUtils.getDiffDay(startDay, endDay);
        for (int i = 0; i < day + 1; i++) {
            Date d = HazeDateUtils.addDays(startDay, i);
            String strDay = HazeDateUtils.getYear(d) + "-" + HazeDateUtils.getMonth(d) + "-" +HazeDateUtils.getDay(d);
            //TODO 模拟数据 后续删除
            int count = new Random().nextInt(10);
            for (Object[] ct : fireCount) {
                String dy = ct[0] + "-" + ct[1] + "-" + ct[2];
                if (strDay.equalsIgnoreCase(dy)) {
                    count = (int) ct[3];
                    break;
                }
            }
            statDays.add(new Object[]{strDay, count});
        }
        result.put("statDays", statDays);
        return result;
    }


    @GetMapping("exportExcel")
    public void exportExcel(@RequestParam(required = false) Date startDay, @RequestParam(required = false) Date endDay, int type,  HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        switch (type) {
            case 0:
                EasyExcel.write(response.getOutputStream(), BusOnOffLog.class).sheet("sheet1").doWrite(vsailStatService.queryOnOffLog(startDay, endDay));
                break;
            case 1:
                EasyExcel.write(response.getOutputStream(), BusFireLog.class).sheet("sheet1").doWrite(vsailStatService.queryFireLog(startDay, endDay));
                break;
            case 2:
                EasyExcel.write(response.getOutputStream(), BusBreakDownLog.class).sheet("sheet1").doWrite(vsailStatService.queryBreakDownLog(startDay, endDay));
                break;
            default:
                //nothing to do
        }
    }



    @GetMapping("testData")
    public String getTestData(Model model) {
        model.addAttribute("dts", vsailStatService.getTestData());
       return "vsail/stat/test";
    }
}
