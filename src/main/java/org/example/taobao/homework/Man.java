package org.example.taobao.homework;

public class Man extends Human {
    public Man() {
        super("Man");
    }

    @Override
    public void live() {
        System.out.println("Man is living.");
    }
}