package org.example.taobao.utils;

import java.util.UUID;

/**
 * @author 关岁安
 */
public class UUIDExample {
    public static void main(String[] args) {
        // 生成随机的UUID
        UUID uuid = UUID.randomUUID();

        // 将UUID转换为字符串
        String uuidString = uuid.toString();

        // 输出UUID字符串
        System.out.println("Generated UUID: " + uuidString);
    }

    public static String gainUUID(){
        // 生成随机的UUID
        UUID uuid = UUID.randomUUID();

        // 将UUID转换为字符串
        String uuidString = uuid.toString();

        // 输出UUID字符串
        System.out.println("Generated UUID: " + uuidString);
        return uuidString;
    }
}