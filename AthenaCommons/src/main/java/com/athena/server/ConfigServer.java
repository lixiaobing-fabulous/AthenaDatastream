package com.athena.server;

import com.athena.rpc.SimpleRpcServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class ConfigServer implements IConfigServer {
    private static Map<String, String> configs = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SimpleRpcServer simpleRpcServer = new SimpleRpcServer(9999);
        ConfigServer configServer = new ConfigServer();
        simpleRpcServer.registerService(IConfigServer.class, configServer);
        simpleRpcServer.start();
    }


    @Override
    public String get(String key) {
        return configs.get(key);
    }

    @Override
    public void put(String key, String value) {
        configs.put(key, value);
        System.out.println(configs);
    }
}
