package com.haze.web;

import com.haze.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private DictService dictService;

    @GetMapping("/")
    public String main(Model model) {
        dictService.findChildsByRootCode("GROUP_TYPE");
        return "index";
    }
}
