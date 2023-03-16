package com.athena.api.util.server;

import lombok.SneakyThrows;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class Client {
    private Socket socket;
    private JavaSerializer javaSerializer;

    @SneakyThrows
    public Client(String host, int port) {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        javaSerializer = new JavaSerializer();
    }

    @SneakyThrows
    public void write(Object object) {
        OutputStream outputStream = socket.getOutputStream();
        IOUtil.writeBytesToOutputStream(outputStream, javaSerializer.serialize(object));
        outputStream.close();
    }
}
