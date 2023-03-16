package com.athena.api.util.server;

import java.io.OutputStream;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class ObjectWriter {
    private JavaSerializer javaSerializer;
    private OutputStream outputStream;

    public ObjectWriter(JavaSerializer javaSerializer, OutputStream outputStream) {
        this.javaSerializer = javaSerializer;
        this.outputStream = outputStream;
    }

    public void write(Object object) {
        IOUtil.writeBytesToOutputStream(outputStream, javaSerializer.serialize(object));
    }
}
