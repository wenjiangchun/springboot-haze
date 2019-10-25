package com.haze.demo.jdk8.lamda;

public class MathOperation {

    public MathOperation(int a, int b) {
        System.out.println("Sum of " + a + " and " + b + " is " + (a + b));
    }

    public MathOperation() {
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
