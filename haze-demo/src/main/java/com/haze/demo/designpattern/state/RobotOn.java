package com.haze.demo.designpattern.state;

public class RobotOn implements RoboticState {

    private final Robot robot;

    public RobotOn(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void on() {
        System.out.println("swithed on......");
    }

    @Override
    public void walk() {
        System.out.println("Walking......");
        robot.setCurrentState(robot.getRobotWalk());
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
