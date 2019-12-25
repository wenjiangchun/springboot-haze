package com.haze.vsail.bus.entity;

import com.haze.core.jpa.entity.AbstractLoginDeletedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 车辆产品信息
 */
@Entity
@Table(name = "v_product")
public class Product extends AbstractLoginDeletedEntity<Long> {

    private String productNum;
    private String vin;
    private String busNum;
    private String control;
    private String display;
    private String probe;
    private String outfire;
    private String line;
    private String transfer;
    private String sim;

    public Product() {
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
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

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getOutfire() {
        return outfire;
    }

    public void setOutfire(String outfire) {
        this.outfire = outfire;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNum='" + productNum + '\'' +
                ", vin='" + vin + '\'' +
                ", busNum='" + busNum + '\'' +
                ", control='" + control + '\'' +
                ", display='" + display + '\'' +
                ", probe='" + probe + '\'' +
                ", outfire='" + outfire + '\'' +
                ", line='" + line + '\'' +
                ", transfer='" + transfer + '\'' +
                ", sim='" + sim + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleteTime=" + deleteTime +
                ", deleted=" + deleted +
                '}';
    }
}