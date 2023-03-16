package com.athena.server;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public interface IConfigServer {
    String get(String key);

    void put(String key, String value);
}
