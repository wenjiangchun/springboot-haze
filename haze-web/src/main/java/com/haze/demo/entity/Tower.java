package com.haze.demo.entity;

import com.haze.core.jpa.entity.SimpleBaseEntity;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="s_tower")
public class Tower extends SimpleBaseEntity<Long> {
    private String siteNum;
    private String name;
    private Point location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getSiteNum() {
        return siteNum;
    }

    public void setSiteNum(String siteNum) {
        this.siteNum = siteNum;
    }

    @Override
    public String toString() {
        return "Tower{" +
                "siteNum='" + siteNum + '\'' +
                "name='" + name + '\'' +
                ", location=" + location.toText() +
                '}';
    }
}
