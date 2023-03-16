package com.athena.server;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.BiConsumer;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class Server {
    private ServerSocket serverSocket;

    @SneakyThrows
    public Server(int port) {
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    public void start(BiConsumer<Object, ObjectWriter> processDataFunction) {
        JavaSerializer javaSerializer = new JavaSerializer();
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                ObjectWriter objectWriter = new ObjectWriter(javaSerializer, outputStream);
                byte[] bytes = IOUtil.getBytesFromInputStream(inputStream);
                Object data = javaSerializer.deserialize(bytes);
                processDataFunction.accept(data, objectWriter);
            }
        }
    }


}
