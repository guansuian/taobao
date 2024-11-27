package org.example.taobao.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.example.taobao.handler.WebSocketServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * @author 关岁安
 */
@Component
@ChannelHandler.Sharable
public class ProjectInitializer extends ChannelInitializer<SocketChannel> {

    static final String WEBSOCKET_PROTOCOL = "WebSocket";

    @Value("${webSocket.netty.path:/webSocket}")
    String webSocketPath;

    @Autowired
    private WebSocketServerHandler webSocketHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 设置管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 流水线管理通道中的处理程序（Handler），用来处理业务
        // webSocket协议本身是基于http协议的，所以这边也要使用http编解码器
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ObjectEncoder());
        // 以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler(webSocketPath, null, true, 65536 * 10,true,true,10000L));
        // 自定义的handler，处理业务逻辑
        pipeline.addLast(webSocketHandler);
    }
}