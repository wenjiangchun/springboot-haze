package com.haze.demo.entity;

import com.haze.core.jpa.entity.SimpleBaseEntity;
import org.locationtech.jts.geom.Point;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="s_tower")
public class Tower extends SimpleBaseEntity<Long> {
    private String siteNum;
    private String name;
    private Point geom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getGeom() {
        return geom;
    }

    public void setGeom(Point geom) {
        this.geom = geom;
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
                ", geom=" + geom.toText() +
                '}';
    }
}
