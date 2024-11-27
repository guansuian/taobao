package org.example.taobao.homework;

public class Woman extends Human {
    public Woman() {
        super("Woman");
    }

    @Override
    public void live() {
        System.out.println("Woman is living.");
    }
}