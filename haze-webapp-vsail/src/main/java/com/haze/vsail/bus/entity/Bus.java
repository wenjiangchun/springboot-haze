package com.haze.vsail.bus.entity;

import com.haze.core.jpa.entity.AbstractLoginDeletedEntity;
import com.haze.system.entity.Group;

import javax.persistence.*;

/**
 * 车辆主机厂信息
 */
@Entity
@Table(name = "v_bus")
public class Bus extends AbstractLoginDeletedEntity<Long> {

    private String vin;
    private String busNum;
    private String registNum;
    private String engineNum;
    private String drivingNum;
    private String motorName;
    private String motorNum;
    private BusModel busModel;
    private Group group;
    private Group rootGroup;

    public Bus() {
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

    public String getMotorName() {
        return motorName;
    }

    public void setMotorName(String motorName) {
        this.motorName = motorName;
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum;
    }

    @ManyToOne
    @JoinColumn(name = "model_id")
    public BusModel getBusModel() {
        return busModel;
    }

    public void setBusModel(BusModel busModel) {
        this.busModel = busModel;
    }

    @ManyToOne
    @JoinColumn(name = "group_id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToOne
    @JoinColumn(name = "root_group_id")
    public Group getRootGroup() {
        return rootGroup;
    }

    public void setRootGroup(Group rootGroup) {
        this.rootGroup = rootGroup;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "vin='" + vin + '\'' +
                ", busNum='" + busNum + '\'' +
                ", registNum='" + registNum + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", drivingNum='" + drivingNum + '\'' +
                ", motorName='" + motorName + '\'' +
                ", motorNum='" + motorNum + '\'' +
                ", busModel=" + busModel +
                ", group=" + group +
                ", rootGroup=" + rootGroup +
                '}';
    }
}