package org.example.taobao.homework;

public class TestNvWaFactory {
    public static void main(String[] args) {
        Human man = NvWaFactory.createHuman("M");
        man.live();

        Human woman = NvWaFactory.createHuman("W");
        woman.live();

        Human robot = NvWaFactory.createHuman("R");
        robot.live();
    }
}