package com.athena.rpc;

import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class SimpleRpcServer {
    private ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();
    private int port;

    public SimpleRpcServer(int port) {
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket socket = server.accept();
            new Thread(() -> invokeInternal(socket)).start();
        }
    }

    public void registerService(Class<?> clazz, Object target) {
        this.serviceMap.put(clazz.getName(), target);
    }

    @SneakyThrows
    private void invokeInternal(Socket socket) {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        try {
            String className = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
            Object[] arguments = (Object[]) objectInputStream.readObject();
            Object target = serviceMap.get(className);
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(target, arguments);
            objectOutputStream.writeObject(result);
        } finally {
            socket.close();
        }

    }
}
