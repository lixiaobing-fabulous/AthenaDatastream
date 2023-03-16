package com.athena.api.util.server;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class JavaSerializer {
    @SneakyThrows
    public byte[] serialize(Object source) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            // Key -> byte[]
            objectOutputStream.writeObject(source);
            return outputStream.toByteArray();
        }
    }

    @SneakyThrows
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            // byte[] -> Value
            return objectInputStream.readObject();
        }
    }


}
