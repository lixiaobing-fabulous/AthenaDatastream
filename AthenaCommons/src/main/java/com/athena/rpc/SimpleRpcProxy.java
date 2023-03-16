package com.athena.rpc;

import com.athena.server.CustomObjectInputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class SimpleRpcProxy {
    public static <T> T create(Class<T> interfaceClass, InetSocketAddress inetSocketAddress, ClassLoader classLoader) {
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, (proxy, method, args) -> {
            try (Socket socket = new Socket(inetSocketAddress.getAddress(), inetSocketAddress.getPort())) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeUTF(interfaceClass.getName());
                out.writeUTF(method.getName());
                out.writeObject(method.getParameterTypes());
                out.writeObject(args);
                ObjectInputStream input = new CustomObjectInputStream(socket.getInputStream(), classLoader);
                if (Void.class.isAssignableFrom(method.getReturnType())) {
                    return null;
                }
                Object result = input.readObject();
                return result;
            }
        });

    }

    public static void main(String[] args) {
    }
}
