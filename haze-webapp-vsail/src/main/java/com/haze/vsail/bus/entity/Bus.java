package com.haze.vsail.bus.entity;

import com.haze.core.jpa.entity.AbstractLoginDeletedEntity;
import com.haze.system.entity.Group;

import javax.persistence.*;
import java.util.Date;

/**
 * 车辆主机厂信息
 */
@Entity
@Table(name = "v_bus")
public class Bus extends AbstractLoginDeletedEntity<Long> {

    private String vin;
    private String busNum;
    private String drivingNum;
    private String modelName;
    private String factoryName;
    private Date assembleDay;
    private String assembleAddress;
    private String productNum;
    private String productBrand;
    private String productType;
    private Boolean used = false;
    private Group rootGroup;
    private Group branchGroup;
    private Group siteGroup;
    private Group lineGroup;

    private Product product;

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

    public String getDrivingNum() {
        return drivingNum;
    }

    public void setDrivingNum(String drivingNum) {
        this.drivingNum = drivingNum;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Date getAssembleDay() {
        return assembleDay;
    }

    public void setAssembleDay(Date assembleDay) {
        this.assembleDay = assembleDay;
    }

    public String getAssembleAddress() {
        return assembleAddress;
    }

    public void setAssembleAddress(String assembleAddress) {
        this.assembleAddress = assembleAddress;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @ManyToOne
    @JoinColumn(name = "root_group_id")
    public Group getRootGroup() {
        return rootGroup;
    }

    public void setRootGroup(Group rootGroup) {
        this.rootGroup = rootGroup;
    }

    @ManyToOne
    @JoinColumn(name = "branch_group_id")
    public Group getBranchGroup() {
        return branchGroup;
    }

    public void setBranchGroup(Group branchGroup) {
        this.branchGroup = branchGroup;
    }

    @ManyToOne
    @JoinColumn(name = "site_group_id")
    public Group getSiteGroup() {
        return siteGroup;
    }

    public void setSiteGroup(Group siteGroup) {
        this.siteGroup = siteGroup;
    }

    @ManyToOne
    @JoinColumn(name = "line_group_id")
    public Group getLineGroup() {
        return lineGroup;
    }

    public void setLineGroup(Group lineGroup) {
        this.lineGroup = lineGroup;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "vin='" + vin + '\'' +
                ", busNum='" + busNum + '\'' +
                ", rootGroup=" + rootGroup +
                '}';
    }
}