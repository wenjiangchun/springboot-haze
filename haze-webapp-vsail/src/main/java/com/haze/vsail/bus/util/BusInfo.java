package com.haze.vsail.bus.util;

import com.haze.common.util.HazeJsonUtils;
import com.haze.vsail.bus.entity.Bus;

import java.io.Serializable;
import java.util.*;

public class BusInfo implements Serializable {

    private String id;

    private String vin;

    private String busNum;

    private String drivingNum;

    private String registNum;

    private String motorNum;

    private String motorName;

    private String groupId;

    private String groupName;

    private String rootGroupId;

    private String rootGroupName;

    private String x;

    private String y;

    private int eventCode = BusEventType.BUS_EVENT_REGISTER.getEventCode();

    /**
     * 是否火警
     */
    private boolean isFire = false;

    /**
     * 是否故障
     */
    private boolean isBreakDown = false;

    /**
     * 是否在线
     */
    private boolean isOnline = false;


    private List<?> sensores = new ArrayList<>();

    private Date sendTime;

    private int sortCode;

    public BusInfo(Bus bus, int eventCode) {
        this.setId(String.valueOf(bus.getId()));
        this.setVin(bus.getVin());
        this.setBusNum(bus.getBusNum());
        this.setDrivingNum(bus.getDrivingNum());
        this.setGroupId(String.valueOf(bus.getGroup().getId()));
        this.setGroupName(String.valueOf(bus.getGroup().getFullName()));
        this.setMotorName(bus.getMotorName());
        this.setMotorNum(bus.getMotorNum());
        this.setRootGroupId(String.valueOf(bus.getRootGroup().getId()));
        this.setRootGroupName(String.valueOf(bus.getRootGroup().getFullName()));
        this.setRegistNum(bus.getRegistNum());
        this.eventCode = eventCode;
    }

    public BusInfo(Map<String, Object> map) {
        if (map.containsKey("id")) {
            this.setId(((String)map.get("id")));
        }
        if (map.containsKey("vin")) {
            this.setVin((String) map.get("vin"));
        }
        if (map.containsKey("busNum")) {
            this.setBusNum((String) map.get("busNum"));
        }
        if (map.containsKey("drivingNum")) {
            this.setDrivingNum((String) map.get("drivingNum"));
        }

        if (map.containsKey("engineNum")) {
            this.setDrivingNum((String) map.get("engineNum"));
        }

        if (map.containsKey("motorName")) {
            this.setMotorName((String) map.get("motorName"));
        }

        if (map.containsKey("motorNum")) {
            this.setMotorNum((String) map.get("motorNum"));
        }

        if (map.containsKey("registNum")) {
            this.setRegistNum((String) map.get("registNum"));
        }

        if (map.containsKey("groupName")) {
            this.setGroupName((String) map.get("groupName"));
        }

        if (map.containsKey("rootGroupName")) {
            this.setRootGroupName((String) map.get("rootGroupName"));
        }

        if (map.containsKey("eventCode")) {
            this.eventCode = Integer.parseInt(map.get("eventCode").toString()) ;
        }
        if (map.containsKey("isFire")) {
            int fire = Integer.parseInt(map.get("isFire").toString());
            this.isFire = fire == 1;
        }
        if (map.containsKey("isBreakDown")) {
            int breakDown = Integer.parseInt(map.get("isBreakDown").toString());
            this.isBreakDown = breakDown == 1;
        }
        if (map.containsKey("sendTime")) {
            this.sendTime = (Date) map.get("sendTime");
        }
        if (map.containsKey("x") && map.containsKey("y")) {
            this.x = (String) map.get("x");
            this.y = (String) map.get("y");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getDrivingNum() {
        return drivingNum;
    }

    public void setDrivingNum(String drivingNum) {
        this.drivingNum = drivingNum;
    }

    public String getRegistNum() {
        return registNum;
    }

    public void setRegistNum(String registNum) {
        this.registNum = registNum;
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum;
    }

    public String getMotorName() {
        return motorName;
    }

    public void setMotorName(String motorName) {
        this.motorName = motorName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRootGroupId() {
        return rootGroupId;
    }

    public void setRootGroupId(String rootGroupId) {
        this.rootGroupId = rootGroupId;
    }

    public String getRootGroupName() {
        return rootGroupName;
    }

    public void setRootGroupName(String rootGroupName) {
        this.rootGroupName = rootGroupName;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public boolean isFire() {
        return isFire;
    }

    public void setFire(boolean fire) {
        isFire = fire;
    }

    public boolean isBreakDown() {
        return isBreakDown;
    }

    public void setBreakDown(boolean breakDown) {
        isBreakDown = breakDown;
    }

    public List<?> getSensores() {
        return sensores;
    }

    public void setSensores(List<?> sensores) {
        this.sensores = sensores;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String toJson() {
        return HazeJsonUtils.writeToString(this);
    }

    public boolean isOnline() {
        if (this.eventCode == BusEventType.BUS_EVENT_ON.getEventCode() || this.eventCode == BusEventType.BUS_EVENT_REAL.getEventCode()) {
            return true;
        }
        return false;
    }

    public int getSortCode() {
        if (isFire) {
            return 1;
        }
        if (isBreakDown) {
            return 2;
        }
        if (isOnline) {
            return 3;
        }
        return 4;
    }

    public static Map<String, Object> fromBus(Bus bus) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", bus.getId().toString());
        map.put("vin", bus.getVin());
        map.put("busNum", bus.getBusNum());
        map.put("drivingNum", bus.getDrivingNum());
        map.put("engineNum", bus.getEngineNum());
        map.put("motorName", bus.getMotorName());
        map.put("motorNum", bus.getMotorNum());
        map.put("groupId", bus.getGroup().getId().toString());
        map.put("rootGroupId", bus.getRootGroup().getId().toString());
        map.put("groupName", bus.getGroup().getFullName());
        map.put("rootGroupName", bus.getRootGroup().getFullName());
        return map;
    }
}
