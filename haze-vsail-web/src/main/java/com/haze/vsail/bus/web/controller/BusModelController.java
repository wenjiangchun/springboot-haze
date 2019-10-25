package com.haze.vsail.bus.web.controller;

import com.haze.vsail.bus.entity.BusEngine;
import com.haze.vsail.bus.entity.BusModel;
import com.haze.vsail.bus.service.BusEngineService;
import com.haze.vsail.bus.service.BusModelService;
import com.haze.web.BaseCrudController;
import com.haze.web.utils.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/v/model")
public class BusModelController extends BaseCrudController<BusModel, Long> {

    private BusModelService busModelService;

    private BusEngineService busEngineService;

    public BusModelController(BusModelService busModelService, BusEngineService busEngineService) {
        super("vsail", "model", "车辆配置", busModelService);
        this.busModelService = busModelService;
        this.busEngineService = busEngineService;
    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
        List<BusEngine> list = this.busEngineService.findAll();
        model.addAttribute("engineList", list);
    }

    @Override
    public WebMessage save(BusModel busModel, HttpServletRequest request) {
        return super.save(busModel, request);
    }
}
