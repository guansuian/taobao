package org.example.taobao.homework;

public abstract class Human {
    protected String type;

    public Human(String type) {
        this.type = type;
    }

    public abstract void live();
}