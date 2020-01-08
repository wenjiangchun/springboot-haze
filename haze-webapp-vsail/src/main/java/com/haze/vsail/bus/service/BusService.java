package com.haze.vsail.bus.service;

import com.haze.common.util.HazeStringUtils;
import com.haze.core.service.AbstractLogicDeletedService;
import com.haze.core.spring.SpringContextUtils;
import com.haze.redis.manage.RedisManager;
import com.haze.shiro.ShiroUser;
import com.haze.shiro.util.ShiroUtils;
import com.haze.system.entity.Dict;
import com.haze.system.entity.Group;
import com.haze.system.service.DictService;
import com.haze.system.service.GroupService;
import com.haze.system.utils.Status;
import com.haze.vsail.bus.dao.BusDao;
import com.haze.vsail.bus.entity.Bus;
import com.haze.vsail.bus.entity.BusModel;
import com.haze.vsail.bus.event.BusEvent;
import com.haze.vsail.bus.util.BusEventType;
import com.haze.vsail.bus.util.BusInfo;
import com.haze.vsail.bus.util.VsailConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BusService extends AbstractLogicDeletedService<Bus, Long> {

    private BusDao busDao;

    private GroupService groupService;

    private DictService dictService;


    private RedisManager redisManager;

    public BusService(BusDao busDao, GroupService groupService, DictService dictService, RedisManager redisManager) {
        super(busDao);
        this.busDao = busDao;
        this.groupService = groupService;
        this.dictService = dictService;
        this.redisManager = redisManager;
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
                    if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_VSAIL_CODE)) { //vsail机构下用户返回所有车辆
                        //TODO 先从缓存中加载
                        busList = this.findAll();
                    } else if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_BUS)) {
                        //获取机构以及机构下所有子机构车辆信息
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

    /**
     * 保存车辆信息
     * @param bus 车辆信息
     * @return 保存后车辆信息
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public Bus saveBusInfo(Bus bus) throws Exception {
        bus =  super.save(bus);
        return bus;
    }

    /**
     * 保存运营信息 同时保存缓信息以及发送车辆信息事件
     * @param bus 车辆信息
     * @return 保存后车辆信息
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public Bus saveBus(Bus bus, BusEventType busEventType) throws Exception {
        bus =  super.save(bus);
        if (bus.getUsed()) {
            Map<String, Object> mapCache = BusInfo.fromBus(bus);
            mapCache.put("eventCode", String.valueOf(busEventType.getEventCode()));
            /*mapCache.put("vin", bus.getVin());
            mapCache.put("id", bus.getId().toString());
            mapCache.put("busNum", bus.getBusNum());
            mapCache.put("drivingNum", bus.getDrivingNum());
            mapCache.put("modelName", bus.getModelName());
            mapCache.put("factoryName", bus.getFactoryName());
            mapCache.put("productNum", bus.getProductNum());
            mapCache.put("rootGroupId", bus.getRootGroup().getId().toString());
            mapCache.put("rootGroupName", bus.getRootGroup().getFullName());
            mapCache.put("branchGroupId", bus.getBranchGroup().getId().toString());
            mapCache.put("branchGroupName", bus.getBranchGroup().getFullName());
            mapCache.put("siteGroupId", bus.getSiteGroup().getId().toString());
            mapCache.put("siteGroupName", bus.getSiteGroup().getFullName());
            mapCache.put("lineGroupId", bus.getLineGroup().getId().toString());
            mapCache.put("lineGroupName", bus.getLineGroup().getFullName());*/
            redisManager.setHash(VsailConstants.BUS_INFO_KEY_PREFFIX + bus.getVin(), mapCache);
            logger.debug("保存到缓存{}", bus);
            //发送车辆信息事件
            SpringContextUtils.publishEvent(new BusEvent(getBusInfoByVin(bus.getVin())));
        }
        return bus;
    }

    /**
     * 获取所有车辆和车辆当前实时信息
     * <p>
     *     首先从缓存中加载车辆基本信息和车辆实时信息,如果缓存不可用则从数据库中加载 TODO 数据库中加载需要考虑并发性能
     * </p>
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BusInfo> getBusData() {
        //首先从缓存中加载 TODO 从数据库中加载
        Set<String> keyList = redisManager.listRedis(VsailConstants.BUS_INFO_KEY_PREFFIX + "*");
        List<BusInfo> busList = new ArrayList<>();
        keyList.forEach(key-> {
            //判断当前用户是否有该车辆权限
            BusInfo busInfo = getBusInfoByVin(HazeStringUtils.replace(key, VsailConstants.BUS_INFO_KEY_PREFFIX, ""));
            if (this.check(busInfo, ShiroUtils.getCurrentUser())) {
                busList.add(busInfo);
            }
            //busList.add(getBusInfoByVin(HazeStringUtils.replace(key, VsailConstants.BUS_INFO_KEY_PREFFIX, "")));
        });
        return busList;
    }

    /**
     * 删除车辆 同时删除车辆缓存信息及发送车辆事件
     * @param bus 车辆信息
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void delete(Bus bus) throws Exception {
        bus.setDeleted(true);
        bus.setDeleteTime(new Date());
        logger.info("logic deleting {}", bus);
        this.busDao.save(bus);
        //如果车辆已运营则删除缓存
        if (bus.getUsed()) {
            redisManager.deleteKey(VsailConstants.BUS_INFO_KEY_PREFFIX + bus.getVin());
            logger.debug("删除缓存{}", bus);
            //发送删除车辆信息事件
            BusInfo busInfo = new BusInfo(bus, BusEventType.BUS_EVENT_DELETE.getEventCode());
            SpringContextUtils.publishEvent(new BusEvent(busInfo));
        }
    }

    /**
     * 根据vin码发送车辆信息变更通知
     * @param vin
     */

    public void sendMessage(String vin) {
        SpringContextUtils.publishEvent(new BusEvent(getBusInfoByVin(vin)));
    }

    @SuppressWarnings("unchecked")
    private BusInfo getBusInfoByVin(String vin) {
        Map<String, Object> info = (Map<String, Object>) redisManager.getKey(VsailConstants.BUS_INFO_KEY_PREFFIX + vin);
        return new BusInfo(info);
    }


    /**
     * 判断用户是否有车辆数据权限
     * @param busInfo 车辆数据
     * @param user 当前登陆用户
     * @return true or false
     */
    public boolean check(BusInfo busInfo, ShiroUser user) {
        if (user == null) {
            return false;
        } else {
            //判断当前用户 如果是系统管理员 则查看所有数据
            if (user.isSuperAdmin()) {
                return true;
            }
            if (user.getGroup() != null) {
                //判断顶级机构是否是福塞尔
                Group g = user.getGroup();
                Group rootGroup = g.getRootGroup();
                if (rootGroup.getCode().equalsIgnoreCase(VsailConstants.GROUP_VSAIL_CODE)) {
                   return true;
                } else {
                    //判断当前车辆是否存在运营信息
                    if (busInfo.getLineGroupId() == null) {
                        return false;
                    } else {
                        Group lineGroup = new Group();
                        lineGroup.setId(Long.valueOf(busInfo.getLineGroupId()));
                        return user.getGroupList().contains(lineGroup);
                    }
                }
            } else {
                return false;
            }
        }
    }
}
