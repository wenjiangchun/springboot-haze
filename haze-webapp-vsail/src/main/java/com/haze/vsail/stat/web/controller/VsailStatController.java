package com.haze.vsail.stat.web.controller;

import com.haze.vsail.stat.entity.BusBreakDownLog;
import com.haze.vsail.stat.entity.BusFireLog;
import com.haze.vsail.stat.entity.BusOnOffLog;
import com.haze.vsail.stat.service.VsailStatService;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v/stat")
public class VsailStatController {

    private VsailStatService vsailStatService;

    public VsailStatController(VsailStatService vsailStatService) {
        this.vsailStatService = vsailStatService;
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
}
