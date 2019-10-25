package com.haze.vsail.bus.entity;

public class ConfigPos {

    private int num;
    private String x;
    private String y;

    public ConfigPos() {
    }

    public ConfigPos(int num, String x, String y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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
}
