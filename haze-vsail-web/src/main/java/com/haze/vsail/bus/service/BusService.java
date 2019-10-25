package com.haze.vsail.bus.service;

import com.haze.core.service.AbstractLogicDeletedService;
import com.haze.redis.manage.RedisManager;
import com.haze.shiro.ShiroUser;
import com.haze.system.entity.Dict;
import com.haze.system.entity.Group;
import com.haze.system.service.DictService;
import com.haze.system.service.GroupService;
import com.haze.system.utils.Status;
import com.haze.vsail.bus.dao.BusDao;
import com.haze.vsail.bus.entity.Bus;
import com.haze.vsail.bus.entity.BusModel;
import com.haze.vsail.bus.util.VsailConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusService extends AbstractLogicDeletedService<Bus, Long> {

    private BusDao busDao;

    private GroupService groupService;

    private DictService dictService;

    private BusModelService busModelService;

    private RedisManager redisManager;

    public BusService(BusDao busDao, GroupService groupService, DictService dictService, RedisManager redisManager, BusModelService busModelService) {
        super(busDao);
        this.busDao = busDao;
        this.groupService = groupService;
        this.dictService = dictService;
        this.redisManager = redisManager;
        this.busModelService = busModelService;
    }

    @Transactional(readOnly = true)
    public List<Group> getBusGroups() {
        List<Group> groupList = new ArrayList<>();
        Dict busDict = dictService.findByParentCodeAndCode(Group.GROUP_TYPE, VsailConstants.GROUP_BUS);
        if (busDict != null) {
            //查询所有属于该机构类型的机构信息
            String hql = "from Group g where g.parent is null and g.groupType=:groupType and g.status=:status";
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("groupType", busDict);
            queryParams.put("status", Status.ENABLE);
            List<Group> rootGroupList = groupService.findByJql(hql, queryParams);
            for (Group root : rootGroupList) {
                groupList.addAll(groupService.getEnabledGroups(root.getCode()));
            }
        }
        return groupList;
    }

    /**
     * 获取用户权限下车辆信息 对于VSAIL部门经理可以获取所有车辆
     * @return
     */
    @Transactional(readOnly = true)
    public List<Bus> getBusesByUser() {
        List<Bus> busList = new ArrayList<>();
        //获取当前登陆信息
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser == null) {
            logger.warn("未登陆用户");
        } else {
            //检测用户是否为超级管理员
            if (shiroUser.isSuperAdmin()) {
                //获取所有的车辆信息 TODO 先从缓存中加载
                busList = this.findAll();
            } else {
                //首先获取用户顶级机构信息
                if (shiroUser.getGroup() == null) {
                    return busList;
                } else {
                    Group rootGroup = shiroUser.getGroup().getRootGroup();
                    if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_VSAIL)) { //vsail机构下用户返回所有车辆
                        //TODO 先从缓存中加载
                        busList = this.findAll();
                    } else if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_BUS)) {
                        //获取机构以及机构下所有子机构车辆信息
                        /*List<Long> groupIds = new ArrayList<>();
                        for (Group group : shiroUser.getGroupList()) {
                            groupIds.add(group.getId());
                        }*/
                        Map<String, Object> queryParams = new HashMap<>();
                        queryParams.put("group_in", shiroUser.getGroupList());
                        //TODO 先从缓存中加载
                        busList = this.findAll(queryParams);
                    } else {
                        //其它部门类型直接返回空列表
                    }
                }
            }
        }
        //TODO 加载完成之后发送websocket
        return busList;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Bus save(Bus bus) throws Exception {
        BusModel busModel = busModelService.findById(bus.getBusModel().getId());
        bus.setBusModel(busModel);
        bus =  super.save(bus);
        //TODO 持久化到redis中
        redisManager.setKey(bus.getVin(), bus);
        logger.debug("保存到缓存");
        return bus;
    }
}
