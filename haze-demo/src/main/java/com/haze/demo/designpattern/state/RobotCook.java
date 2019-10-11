package com.haze.demo.designpattern.state;

public class RobotCook implements RoboticState {

    private final Robot robot;

    public RobotCook(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void on() {
        System.out.println("Already on......");
    }

    @Override
    public void walk() {
        System.out.println("Walking......");
        robot.setCurrentState(robot.getRobotWalk());
    }

    @Override
    public void cook() {
        System.out.println("Cooking......");
    }

    @Override
    public void off() {
        System.out.println("Cannot switched off while cooking......");
    }
}
