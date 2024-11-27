package org.example.taobao.homework;

public class Robot extends Human {
    public Robot() {
        super("Robot");
    }

    @Override
    public void live() {
        System.out.println("Robot is operating.");
    }
}