package com.haze.demo.designpattern.state;

public class RobotOff implements RoboticState {

    private final Robot robot;

    public RobotOff(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void on() {
        System.out.println("Swithed on ......");
        robot.setCurrentState(robot.getRobotOn());
    }

    @Override
    public void walk() {
        System.out.println("Cannot walk at Off state......");
    }

    @Override
    public void cook() {
        System.out.println("Cannot cook at Off state......");

    }

    @Override
    public void off() {
        System.out.println("Already switched off......");
    }
}
