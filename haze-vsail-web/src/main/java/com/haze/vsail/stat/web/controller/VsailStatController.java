package com.haze.vsail.stat.web.controller;

import com.haze.core.spring.SpringContextUtils;
import com.haze.vsail.stat.entity.BusOnOffLog;
import com.haze.vsail.stat.entity.BusTest;
import com.haze.vsail.stat.service.BusTestService;
import com.haze.vsail.stat.service.VsailStatService;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        System.out.println(Integer.toBinaryString((0x00 & 0xFF) + 0x100).substring(1));
        BusTestService busTestService = SpringContextUtils.getBean(BusTestService.class);
        BusTest busTest = new BusTest();
        busTest.setGroupId(0L);
        try {
            busTestService.save(busTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* BusTest busTest2 = new BusTest();
        busTest2.setId(2L);
        busTest2.setGroupId(1L);
        try {
            busTestService.save(busTest2);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        PageRequest p = PageRequest.of(0, 1, Sort.by("id"));
        busTestService.findByProperty("groupId", 2L);
        return "vsail/stat/onoff";
    }

    @PostMapping(value = "searchOnOff")
    @ResponseBody
    public DataTablePage search(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        Page<BusOnOffLog> list = this.vsailStatService.findOnOffPage(p, dataTableParams.getQueryVairables());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }
}
