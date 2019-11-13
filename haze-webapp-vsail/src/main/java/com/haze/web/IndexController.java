package com.haze.web;

import com.haze.system.entity.Config;
import com.haze.system.service.ConfigService;
import com.haze.system.service.DictService;
import com.haze.vsail.bus.util.VsailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private DictService dictService;

    @Autowired
    private ConfigService configService;

    @GetMapping("/")
    public String main(Model model) {
        dictService.findChildsByRootCode("GROUP_TYPE");
        Config config = configService.findByCode(VsailConstants.WEB_SOCKET_CONFIG_CODE);
        model.addAttribute("config", config);
        return "index";
    }
}
