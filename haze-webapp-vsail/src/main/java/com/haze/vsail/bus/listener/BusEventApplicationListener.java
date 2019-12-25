package com.haze.vsail.bus.listener;

import com.haze.core.spring.SpringContextUtils;
import com.haze.shiro.ShiroUser;
import com.haze.shiro.util.ShiroUtils;
import com.haze.system.entity.Group;
import com.haze.system.entity.User;
import com.haze.vsail.bus.event.BusEvent;
import com.haze.vsail.bus.util.BusInfo;
import com.haze.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 车辆消息监听类，在车辆信息变更后(包括车辆增删改以及车辆实时位置信息变化)由该类统一负责向websocket中发送消息, 所有连接到该系统的websocket客户端
 * 收到车辆变更事件后更新车辆信息
 */
public class BusEventApplicationListener implements ApplicationListener<BusEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BusEventApplicationListener.class);

    @Override
    public void onApplicationEvent(@NotNull BusEvent event) {
        WebSocketServer socketServer = SpringContextUtils.getBean(WebSocketServer.class);
        logger.debug("车辆信息发生变化, event={}", event);
        //获取已连接对象
        Map<String, WebSocketServer> connectedMap = WebSocketServer.getConnectedMap();
        //判断连接对象是否有查看该车权限
        BusInfo busInfo = event.getBusInfo();
        List<ShiroUser> onlineList = ShiroUtils.getOnlineUserList();
        connectedMap.forEach((k,v) -> {
            //获取用户信息
            ShiroUser user = getShiroUser(onlineList, k);
            //判断该用户是否有对该信息具有查看权限
            if (Objects.nonNull(user) && checkBus(user, busInfo)) {
               //发送消息
                socketServer.sendMessageToName(busInfo.toJson(), k);
            }
        });
    }

    private ShiroUser getShiroUser(List<ShiroUser> shiroUserList, String loginName) {
        for (ShiroUser shiroUser : shiroUserList) {
            if (shiroUser.getLoginName().equals(loginName)) {
                return shiroUser;
            }
        }
        return null;
    }

    private boolean checkBus(ShiroUser user, BusInfo busInfo) {
        if (user.getLoginName().equals(User.ADMIN)) {
            return true;
        }
        Group g = new Group();
        g.setId(Long.valueOf(busInfo.getLineGroupId()));
        return user.getGroupList().contains(g);
    }
}