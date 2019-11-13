package com.haze.vsail;

import com.haze.vsail.bus.service.BusService;
import com.haze.vsail.bus.util.BusEventType;
import com.haze.vsail.bus.util.BusInfo;
import com.haze.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("v")
@Controller
public class VsailController extends BaseController {

    private BusService busService;

    public VsailController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("/map")
    public String map(Model model) {
        //TODO 根据用户查询属于该用户下的具体车辆信息
        return "vsail/map";
    }

    @GetMapping("/monitorCell/{busId}")
    public String monitorCell(@PathVariable Long busId, Model model) {
        model.addAttribute("busId", busId);
        return "vsail/cell";
    }

    /**
     * 发送信息至当前已连接的websocket客户端
     * @param vin 信息内容
     */
    @PostMapping("/sendBusMessage")
    public void sendMessage(@RequestParam String vin) {
        busService.sendMessage(vin);
    }

    @PostMapping("/getBusData")
    @ResponseBody
    public List<BusInfo> sendMessage() {
        return busService.getBusData();
    }
}
