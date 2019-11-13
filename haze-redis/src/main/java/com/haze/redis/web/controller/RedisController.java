package com.haze.redis.web.controller;

import com.haze.common.util.HazeJsonUtils;
import com.haze.redis.manage.RedisManager;
import com.haze.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/m/redis")
public class RedisController extends BaseController {

    private RedisManager redisManager;

    public RedisController(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("keys", redisManager.listRedis());
        return "/m/redis/list";
    }

    @GetMapping("/get/{key}")
    @ResponseBody
    public Object getKey(@PathVariable String key) {
        return HazeJsonUtils.writeToString(redisManager.getKey(key));
    }

    @PostMapping("/delete/{key}")
    @ResponseBody
    public String deleteKey(@PathVariable String key) {
        redisManager.deleteKey(key);
         return "SUCCESS";
    }

    @PostMapping("/set/{key}")
    @ResponseBody
    public String setKey(@PathVariable String key, @RequestParam String value) {
        redisManager.setKey(key, value);
        return "SUCCESS";
    }
}
