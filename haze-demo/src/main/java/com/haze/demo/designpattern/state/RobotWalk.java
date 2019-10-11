package com.haze.demo.designpattern.state;

public class RobotWalk implements RoboticState {

    private final Robot robot;

    public RobotWalk(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void on() {
        System.out.println("Robot is Already on......");
    }

    @Override
    public void walk() {
        System.out.println("Walking......");
    }

    @Override
    public void cook() {
        System.out.println("Cooking......");
        robot.setCurrentState(robot.getRobotCook());
    }

    @Override
    public void off() {
        System.out.println("Robot is switched off......");
        robot.setCurrentState(robot.getRobotOff());
    }
}
