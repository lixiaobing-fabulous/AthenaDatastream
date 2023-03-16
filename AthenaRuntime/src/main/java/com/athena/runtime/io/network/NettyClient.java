package com.athena.runtime.io.network;

import com.athena.runtime.io.network.codec.MessageFrameEncoder;
import com.athena.runtime.io.network.codec.MessageProtocolEncoder;
import com.athena.runtime.io.network.exception.ExceptionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class NettyClient {
    private Bootstrap bootstrap;
    private EventLoopGroup group;

    public NettyClient() {
        init();
    }

    public void init() {
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        this.bootstrap.group(group).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, false).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("frame-encoder", new MessageFrameEncoder()).addLast("protocol-encoder", new MessageProtocolEncoder()).addLast(new ExceptionHandler());
            }
        });


    }
}
