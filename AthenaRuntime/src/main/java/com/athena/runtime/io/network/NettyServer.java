package com.athena.runtime.io.network;

import com.athena.api.util.IpUtil;
import com.athena.runtime.io.network.codec.MessageFrameDecoder;
import com.athena.runtime.io.network.codec.MessageProtocolDecoder;
import com.athena.runtime.io.network.exception.ExceptionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Slf4j
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public NettyServer() {
        this(6666);
    }

    private ServerBootstrap bootstrap;

    private EventLoopGroup group;

    private NioEventLoopGroup workerGroup;

    private Channel channel;

    public NettyServer start() {
        this.bootstrap = new ServerBootstrap();
        this.group = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        bootstrap.group(group, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("frame-decoder", new MessageFrameDecoder())
                                .addLast("protocol-decoder", new MessageProtocolDecoder())
                                .addLast(new ExceptionHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(port);
        try {
            channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {
            log.error("interrupted exception", e);
        }
        log.info("netty server 启动 host: {} 端口: {}", IpUtil.getLocalIp(), port);
        return this;
    }
}
