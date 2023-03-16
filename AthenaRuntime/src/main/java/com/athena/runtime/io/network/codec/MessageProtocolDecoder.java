package com.athena.runtime.io.network.codec;

import com.athena.runtime.io.network.codec.serializer.JavaSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description message protocol decoder
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Slf4j
public class MessageProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int size = msg.readInt();
        byte[] data = new byte[size];
        msg.readBytes(data);
        Object object = new JavaSerializer().deserialize(data);
        out.add(object);
        log.debug("Serialize from bytes[length:{}] to be a {}", size, object);

    }
}
