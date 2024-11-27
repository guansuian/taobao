package org.example.taobao.homework;

public class NvWaFactory {
    public static Human createHuman(String type) {
        switch (type) {
            case "M":
                return new Man();
            case "W":
                return new Woman();
            case "R":
                return new Robot();
            default:
                throw new IllegalArgumentException("Unknown human type: " + type);
        }
    }
}