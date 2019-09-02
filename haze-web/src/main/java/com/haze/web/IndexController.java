package com.haze.web;

import com.haze.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Properties;

@Controller
public class IndexController {

    @Autowired
    private DictionaryService dictionaryService;
    @GetMapping("/")
    public String index(Model model) {
        dictionaryService.findChildsByRootCode("GROUP_TYPE");
        return "index";
    }

}
