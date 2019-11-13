package com.haze.vsail.stat.entity;

import com.haze.vsail.bus.util.BusEventType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "v_bus_on_off_log")
public class BusOnOffLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    protected Long id;

    private String vin;

    private String busNum;

    private String registNum;

    private String engineNum;

    private String drivingNum;

    private String groupName;

    private String rootGroupName;

    private Date logTime;

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer flag = BusEventType.BUS_EVENT_ON.getEventCode();

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getRegistNum() {
        return registNum;
    }

    public void setRegistNum(String registNum) {
        this.registNum = registNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getDrivingNum() {
        return drivingNum;
    }

    public void setDrivingNum(String drivingNum) {
        this.drivingNum = drivingNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRootGroupName() {
        return rootGroupName;
    }

    public void setRootGroupName(String rootGroupName) {
        this.rootGroupName = rootGroupName;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Column(name = "log_year")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Column(name = "log_month")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Column(name = "log_day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "BusOnOffLog{" +
                "vin='" + vin + '\'' +
                ", busNum='" + busNum + '\'' +
                ", registNum='" + registNum + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", drivingNum='" + drivingNum + '\'' +
                ", groupName='" + groupName + '\'' +
                ", rootGroupName='" + rootGroupName + '\'' +
                ", logTime=" + logTime +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", flag=" + flag +
                ", id=" + id +
                '}';
    }
}
