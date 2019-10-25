package com.haze.vsail.bus.entity;

import com.haze.core.jpa.entity.AbstractLoginDeletedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 车辆主机厂信息
 */
@Entity
@Table(name = "v_bus_engine")
public class BusEngine extends AbstractLoginDeletedEntity<Long> {

    private String name;
    private String address;
    private String linker;
    private String mobile;
    private String description;

    public BusEngine() {
    }

    @Column(unique = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 20)
    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker;
    }

    @Column(length = 11)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BusEngine{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", linker='" + linker + '\'' +
                ", mobile='" + mobile + '\'' +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}