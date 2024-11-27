package org.example.taobao.utils;

import org.junit.jupiter.api.Test;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author 关岁安
 */
public class IdGenerationTest {


    public static Long generateId() {
        Snowflake snowflake = IdUtil.createSnowflake(0, 1);
        long id;

        // 无限循环直到找到一个有效的ID
        while (true) {
            id = snowflake.nextId();
//          System.out.println("Original ID: " + id);
            // 将ID转换为字符串并反转
            StringBuilder sb = new StringBuilder(String.valueOf(id));
            StringBuilder reverse = sb.reverse();
            try {
                // 尝试将反转后的字符串转换为Long
                long reversedId = Long.parseLong(reverse.toString());

                // 除以1000
                long shortenedId = reversedId / 1000;

                // 确保shortenedId在Integer范围内
                while (shortenedId > Integer.MAX_VALUE) {
                    shortenedId /= 10;
                }

                // 将shortenedId转换为Integer
                Long _id_ =  shortenedId;
//                System.out.println("Shortened ID: " + _id_);
                return _id_;
            } catch (NumberFormatException e) {
                System.err.println("在使用雪花算法的时候出现了问题 但是问题不大 不需要检查 " + reverse.toString());
                // 如果出现异常，继续循环生成新的ID
            }
        }
    }
}