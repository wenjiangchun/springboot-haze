package com.haze.demo.designpattern.state;

/**
 * 状态模式之机器人状态接口定义
 */
public interface RoboticState {

    void on();

    void walk();

    void cook();

    void off();
}
