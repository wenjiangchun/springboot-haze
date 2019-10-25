package com.haze.vsail.stat.entity;

import com.haze.core.jpa.entity.AbstractCustomIDEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "v_bus_test")
public class BusTest extends AbstractCustomIDEntity<Long> {

    private static final long serialVersionUID = 1L;


    private Long groupId;

    private String name;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BusTest{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
