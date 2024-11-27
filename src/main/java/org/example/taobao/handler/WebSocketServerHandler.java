package org.example.taobao.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.example.taobao.pojo.Message;
import org.example.taobao.service.impl.WebSocketServiceImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 关岁安
 */
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static ConcurrentHashMap<String, ChannelHandlerContext> userChannels = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String message = msg.text();
        Message.MessageType messageType = null;
        Message chatMessage = objectMapper.readValue(message, Message.class);
        switch (chatMessage.getType()) {
            case AuditGoods:
                System.out.println("发来了一条处理商品的信息");
//                webSocketService.sendNeedAuditGoods(chatMessage,ctx);
                WebSocketServiceImpl.sendNeedAuditGoods(chatMessage,ctx);
                break;
            default:
                System.out.println("未处理的消息类型: " + chatMessage.getType());
                // 你可以选择是否向客户端反馈未处理的消息类型
                break;
        }
        System.out.println(chatMessage);
        // 广播消息给所有连接的客户端
        ctx.channel().writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(chatMessage)));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("有新的客户连接进行来了 " + ctx.channel().id().asLongText());

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            WebSocketServerProtocolHandler.HandshakeComplete complete=(WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = complete.requestUri();
            String userId = extractUserId(url);
            System.out.println(userId);
            if (userId != null){
                System.out.println("添加进一个用户");
                userChannels.put(userId,ctx);
            }
        }
    }
    private String extractUserId(String uri) {
        if (uri.contains("?")) {
            String query = uri.split("\\?")[1];
            String[] params = query.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("移除了一个用户: " + ctx.channel().id().asLongText());
        for (String id : userChannels.keySet()) {
            if (userChannels.get(id).equals(ctx)) {
                userChannels.remove(id);
                System.out.println("移除了一个客户");
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
