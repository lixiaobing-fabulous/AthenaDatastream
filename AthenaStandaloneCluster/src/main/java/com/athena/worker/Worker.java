package com.athena.worker;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
@Data
public class Worker implements Serializable {
    private String host = "localhost";
    private int port;

    public Worker(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Worker(int port) {
        this.port = port;
    }
}
