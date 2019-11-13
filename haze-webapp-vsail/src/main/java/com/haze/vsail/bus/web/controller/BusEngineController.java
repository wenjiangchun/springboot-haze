package com.haze.vsail.bus.web.controller;

import com.haze.vsail.bus.entity.BusEngine;
import com.haze.vsail.bus.service.BusEngineService;
import com.haze.web.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/v/engine")
public class BusEngineController extends BaseCrudController<BusEngine, Long> {

    private BusEngineService busEngineService;

    public BusEngineController(BusEngineService service) {
        super("vsail", "engine", "主机厂", service);
        this.busEngineService = service;
    }

}
