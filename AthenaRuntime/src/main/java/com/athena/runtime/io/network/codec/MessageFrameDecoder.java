package com.athena.runtime.io.network.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Description 官方实现的完善的消息处理解析器
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class MessageFrameDecoder extends LengthFieldBasedFrameDecoder {
    public MessageFrameDecoder() {
        super(1024000, 0, 4, 0, 0);
    }
}
