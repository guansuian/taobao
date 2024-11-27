package org.example.taobao.vo;

import lombok.Data;
import org.example.taobao.pojo.Message;

/**
 * @author 关岁安
 */
@Data
public class MessageVo {
    private String sender;
    private String receiver;
    private String content;
    private MessageVoType type;

    public MessageVo(String sender, String receiver, String content, MessageVoType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
    }

    public enum MessageVoType {
        //审核需要审核创建的商品
        AuditIsNull,
        AuditNewGoods

    }

}
