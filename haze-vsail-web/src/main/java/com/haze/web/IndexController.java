package com.haze.web;

import com.haze.system.entity.Config;
import com.haze.system.service.ConfigService;
import com.haze.system.service.DictService;
import com.haze.vsail.bus.util.VsailConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController{

    private final ConfigService configService;

    private final DictService dictService;

    public IndexController(ConfigService configService, DictService dictService) {
        this.configService = configService;
        this.dictService = dictService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Config config = configService.findByCode(VsailConstants.BAIDU_MAP_KEY);
        model.addAttribute("mapConfig", config);

        dictService.findChildsByRootCode("SSS");
        //dictionaryService.findByRootCodeAndCode("GROUP_TYPE","aaa");
        return "index";
    }

}
