package com.athena.rpc;

import java.net.InetSocketAddress;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class HelloRpcClient {
    public static void main(String[] args) {
        HelloRpc helloRpc = SimpleRpcProxy.create(HelloRpc.class, new InetSocketAddress("localhost", 1234), HelloRpcClient.class.getClassLoader());
        System.out.println(helloRpc.hello("world"));
    }
}
