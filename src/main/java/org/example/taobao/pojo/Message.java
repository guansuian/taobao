package org.example.taobao.pojo;

import lombok.Data;

/**
 * @author 关岁安
 */

@Data
public class Message {
    private String sender;
    private String receiver;
    private String content;
    private MessageType type;

    public enum MessageType {
        //审核需要审核创建的商品
       AuditGoods,
    }

    // Getters and Setters
}
