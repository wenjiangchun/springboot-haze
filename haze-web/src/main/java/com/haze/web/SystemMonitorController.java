package com.haze.web;

import com.haze.common.os.SystemInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.*;

@Controller
@RequestMapping("/monitor")
public class SystemMonitorController extends BaseController{

    @GetMapping("/jvm/view")
    public String jvm(Model model) {
        List<SystemInfo> systemInfoList = new ArrayList<>();
        Properties p = System.getProperties();
        System.getenv().forEach((k,v)-> {
            SystemInfo info = new SystemInfo(k, v);
            systemInfoList.add(info);
        });

        Runtime runtime = Runtime.getRuntime();
        //获取可用处理器信息
        SystemInfo processorInfo = new SystemInfo("availableProcessors", String.valueOf(runtime.availableProcessors()), "可用处理器数");
        systemInfoList.add(processorInfo);
        systemInfoList.add(new SystemInfo("freeMemory", runtime.freeMemory() / (1024 * 1024) + "MB", "可用内存"));
        systemInfoList.add(new SystemInfo("maxMemory", runtime.maxMemory() / (1024 * 1024) + "MB", "最大内存"));
        systemInfoList.add(new SystemInfo("totalMemory", runtime.totalMemory() / (1024 * 1024) + "MB", "总内存"));
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        systemInfoList.add(new SystemInfo("runtimeName[pid@hostname]", runtimeMXBean.getName(), "进程名称"));
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        systemInfoList.add(new SystemInfo("heapInitMemory", memoryMXBean.getHeapMemoryUsage().getInit() / (1024 * 1024) + "MB", "已使用堆内存"));
        systemInfoList.add(new SystemInfo("heapUsedMemory", memoryMXBean.getHeapMemoryUsage().getUsed() / (1024 * 1024) + "MB", "已使用堆内存"));
        systemInfoList.add(new SystemInfo("heapMaxMemory", memoryMXBean.getHeapMemoryUsage().getMax()/ (1024 * 1024) + "MB", "最大堆内存"));
        //ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        model.addAttribute("infoList", systemInfoList);
        return "/monitor/jvm";
    }

    /*@GetMapping("/ssh/view")
    public String sshConsole(Model model) {
        LinuxSSH ssh = new LinuxSSH();
        return "/monitor/sshConsole";
    }

    @PostMapping("/ssh/exec")
    @ResponseBody
    public List<String> execCommand(Model model, @RequestParam String command) {
        try {
            List<String> list = ssh.execute(command);
            return list;
        } catch (Exception e) {
            logger.error("命令执行失败", e);
            return Lists.newArrayList();
        }
    }*/
}
