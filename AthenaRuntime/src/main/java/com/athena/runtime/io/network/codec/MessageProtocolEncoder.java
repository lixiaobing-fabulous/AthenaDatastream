package com.athena.runtime.io.network.codec;

import com.athena.runtime.io.network.codec.serializer.JavaSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description 消息
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Slf4j
public class MessageProtocolEncoder extends MessageToMessageEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] data = new JavaSerializer().serialize(msg);
        buffer.writeBytes(data);
        log.debug("Encode {} to bytes[length:{}]", msg, data.length);
        out.add(buffer);
    }
}
