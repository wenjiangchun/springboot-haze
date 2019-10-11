package com.haze.demo.designpattern.state;

public class Robot implements RoboticState {
    private RoboticState robotOn;

    private RoboticState robotCook;
    private RoboticState robotOff;
    private RoboticState robotWalk;

    private RoboticState currentState;

    public Robot() {
        this.robotOn = new RobotOn(this);
        this.robotWalk = new RobotWalk(this);
        this.robotCook = new RobotCook(this);
        this.robotOff = new RobotOff(this);
        this.currentState = robotOff;
    }

    public void setCurrentState(RoboticState currentState) {
        this.currentState = currentState;
    }

    @Override
    public void on() {
        currentState.on();
    }

    @Override
    public void walk() {
        currentState.walk();
    }

    @Override
    public void cook() {
        currentState.cook();
    }

    @Override
    public void off() {
        currentState.off();
    }

    public RoboticState getRobotOn() {
        return robotOn;
    }

    public RoboticState getRobotCook() {
        return robotCook;
    }

    public RoboticState getRobotOff() {
        return robotOff;
    }

    public RoboticState getRobotWalk() {
        return robotWalk;
    }
}
