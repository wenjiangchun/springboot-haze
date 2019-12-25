package com.haze.vsail.bus.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haze.common.util.HazeDateUtils;
import com.haze.common.util.HazeJsonUtils;
import com.haze.vsail.bus.entity.Bus;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

public class BusInfo implements Serializable {

    private String id;
    private String vin;
    private String busNum;
    private String drivingNum;
    private String productNum;
    private String rootGroupId;
    private String modelName;
    private String rootGroupName;
    private String branchGroupId;
    private String branchGroupName;
    private String siteGroupId;
    private String siteGroupName;
    private String lineGroupId;
    private String lineGroupName;
    private String address;
    private String linker;
    private String linkerMobile;
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

    private List<Sensor> sensores = new ArrayList<>();

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    public BusInfo(Bus bus, int eventCode) {
        this(BusInfo.fromBus(bus));
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

        if (map.containsKey("rootGroupName")) {
            this.setRootGroupName((String) map.get("rootGroupName"));
        }

        if (map.containsKey("lineGroupName")) {
            this.setLineGroupName((String) map.get("lineGroupName"));
        }

        if (map.containsKey("siteGroupName")) {
            this.setSiteGroupName((String) map.get("siteGroupName"));
        }

        if (map.containsKey("branchGroupName")) {
            this.setBranchGroupName((String) map.get("branchGroupName"));
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
        if (map.containsKey("stime")) {
            try {
                this.sendTime = HazeDateUtils.parseDate((String)map.get("stime"), "yyyy-MM-dd hh:mm:ss") ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (map.containsKey("x") && map.containsKey("y")) {
            this.x = (String) map.get("x");
            this.y = (String) map.get("y");
        }
        if (map.containsKey("modelName")) {
            this.modelName = (String)map.get("modelName");
        }
        if (map.containsKey("address")) {
            this.address = (String)map.get("address");
        }
        if (map.containsKey("linker")) {
            this.linker = (String)map.get("linker");
        }
        if (map.containsKey("linkerMobile")) {
            this.linkerMobile = (String)map.get("linkerMobile");
        }
        if (map.containsKey("sensores")) {
            String str = (String)map.get("sensores");
            str = str.replaceAll("'","\"");
            //Sensor[] a = HazeJsonUtils.readFromString(str, Sensor[].class);
            this.sensores = HazeJsonUtils.readFromString(str, sensores.getClass());
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

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getBranchGroupId() {
        return branchGroupId;
    }

    public void setBranchGroupId(String branchGroupId) {
        this.branchGroupId = branchGroupId;
    }

    public String getBranchGroupName() {
        return branchGroupName;
    }

    public void setBranchGroupName(String branchGroupName) {
        this.branchGroupName = branchGroupName;
    }

    public String getSiteGroupId() {
        return siteGroupId;
    }

    public void setSiteGroupId(String siteGroupId) {
        this.siteGroupId = siteGroupId;
    }

    public String getSiteGroupName() {
        return siteGroupName;
    }

    public void setSiteGroupName(String siteGroupName) {
        this.siteGroupName = siteGroupName;
    }

    public String getLineGroupId() {
        return lineGroupId;
    }

    public void setLineGroupId(String lineGroupId) {
        this.lineGroupId = lineGroupId;
    }

    public String getLineGroupName() {
        return lineGroupName;
    }

    public void setLineGroupName(String lineGroupName) {
        this.lineGroupName = lineGroupName;
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

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker;
    }

    public String getLinkerMobile() {
        return linkerMobile;
    }

    public void setLinkerMobile(String linkerMobile) {
        this.linkerMobile = linkerMobile;
    }

    public boolean isOnline() {
        if (this.eventCode == BusEventType.BUS_EVENT_ON.getEventCode() || this.eventCode == BusEventType.BUS_EVENT_REAL.getEventCode()) {
            return true;
        }
        return false;
    }

    public static Map<String, Object> fromBus(Bus bus) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", bus.getId().toString());
        map.put("vin", bus.getVin());
        map.put("busNum", bus.getBusNum());
        map.put("modelName", bus.getModelName());
        map.put("factoryName", bus.getFactoryName());
        map.put("drivingNum", bus.getDrivingNum());
        map.put("productNum", bus.getProductNum());
        map.put("rootGroupId", bus.getRootGroup().getId());
        map.put("rootGroupName", bus.getRootGroup().getFullName());
        map.put("branchGroupId", bus.getBranchGroup().getId());
        map.put("branchGroupName", bus.getBranchGroup().getFullName());
        map.put("siteGroupId", bus.getSiteGroup().getId());
        map.put("siteGroupName", bus.getSiteGroup().getFullName());
        map.put("lineGroupId", bus.getLineGroup().getId());
        map.put("lineGroupName", bus.getLineGroup().getFullName());
        map.put("address", bus.getSiteGroup().getAddress());
        map.put("linker", bus.getSiteGroup().getLinker());
        map.put("linkerMobile", bus.getSiteGroup().getLinkerMobile());
        return map;
    }


    public static class Sensor {
        private int sn;
        private String fire;
        private String error;
        private int concen;
        private int temp;

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }

        public String getFire() {
            return fire;
        }

        public void setFire(String fire) {
            this.fire = fire;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public int getConcen() {
            return concen;
        }

        public void setConcen(int concen) {
            this.concen = concen;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
    }
}
