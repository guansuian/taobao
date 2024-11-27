package org.example.taobao.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.example.taobao.pojo.Message;
import org.example.taobao.vo.MessageVo;
import org.springframework.stereotype.Service;

import static org.example.taobao.handler.WebSocketServerHandler.userChannels;


/**
 * @author 关岁安
 */
@Service
public class WebSocketServiceImpl {

    public static void sendNeedAuditGoods(Message message,ChannelHandlerContext ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String sender = message.getSender();
        String receiver = message.getReceiver();
        String content = message.getContent();
        ChannelHandlerContext receiverCx = userChannels.get("7");
        if(receiverCx == null){
            MessageVo messageVo = new MessageVo(sender,receiver,content, MessageVo.MessageVoType.AuditIsNull);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(messageVo)));
        }else{
            MessageVo messageVo = new MessageVo(sender,receiver,content, MessageVo.MessageVoType.AuditNewGoods);
            receiverCx.channel().writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(messageVo)));
        }

    }

}
