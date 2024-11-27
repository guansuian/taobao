package org.example.taobao.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.taobao.handler.WebSocketServerHandler;
import org.springframework.stereotype.Component;
/**
 * @author 关岁安
 */
@Component
@Slf4j
public class NettyConfig{

    private final int port = 8888;


    @PostConstruct
    public void start() throws InterruptedException {
        new Thread(()->{
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
                        .localAddress(8888)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new ChunkedWriteHandler());
                                ch.pipeline().addLast(new ObjectEncoder());
                                ch.pipeline().addLast(new HttpObjectAggregator(8192));
                                ch.pipeline().addLast(new WebSocketServerProtocolHandler("/webSocket"));
                                ch.pipeline().addLast(new WebSocketServerHandler());
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                try {
                    ChannelFuture future = serverBootstrap.bind(port).sync();
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }
}
