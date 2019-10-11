package com.haze.demo.designpattern.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RobotTest {
    private Robot robot;

    @Before
    public void setUp() throws Exception {
        robot = new Robot();
    }

    @Test
    public void testRobot() {
        robot.cook();
        robot.off();
        robot.walk();
        robot.off();
        robot.on();
        robot.walk();
    }
}