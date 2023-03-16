package com.athena.api.util.server;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class IOUtil {
    @SneakyThrows
    public static byte[] getBytesFromInputStream(InputStream inputStream) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    @SneakyThrows
    public static void writeBytesToOutputStream(OutputStream outputStream, byte[] data) {
        outputStream.write(data, 0, data.length);
        outputStream.flush();
    }

}
