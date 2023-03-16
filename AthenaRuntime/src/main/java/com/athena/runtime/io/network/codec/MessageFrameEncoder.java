package com.athena.runtime.io.network.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Description 官方实现的FrameEncoder
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class MessageFrameEncoder extends LengthFieldPrepender {
    public MessageFrameEncoder() {
        super(4);
    }
}
