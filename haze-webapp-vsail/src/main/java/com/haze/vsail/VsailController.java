package com.haze.vsail;

import com.haze.vsail.bus.service.BusService;
import com.haze.vsail.bus.util.BusEventType;
import com.haze.vsail.bus.util.BusInfo;
import com.haze.vsail.stat.service.VsailStatService;
import com.haze.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("v")
@Controller
public class VsailController extends BaseController {

    private BusService busService;

    private VsailStatService vsailStatService;

    public VsailController(BusService busService, VsailStatService vsailStatService) {
        this.busService = busService;
        this.vsailStatService = vsailStatService;
    }

    @GetMapping("/map")
    public String map(Model model) {
        //TODO 根据用户查询属于该用户下的具体车辆信息
        return "vsail/map";
    }

    @GetMapping("/monitorCell/{busId}/{vin}")
    public String monitorCell(@PathVariable Long busId, @PathVariable String vin, Model model) {
        model.addAttribute("busId", busId);
        model.addAttribute("vin", vin);
        return "vsail/cell";
    }

    /**
     * 发送信息至当前已连接的websocket客户端
     * @param vin 信息内容
     */
    @PostMapping("/public/sendBusMessage")
    @ResponseBody
    public void sendMessage(@RequestParam String vin) {
        busService.sendMessage(vin);
    }

    @PostMapping("/getBusData")
    @ResponseBody
    public List<BusInfo> sendMessage() {
        return busService.getBusData();
    }


    @GetMapping("/public/testData")
    public String getTestData(Model model) {
        model.addAttribute("dts", vsailStatService.getTestData());
        return "vsail/stat/test";
    }
}
