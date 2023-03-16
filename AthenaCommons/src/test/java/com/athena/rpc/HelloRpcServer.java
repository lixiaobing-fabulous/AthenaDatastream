package com.athena.rpc;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class HelloRpcServer {
    public static void main(String[] args) {
        HelloRpcImpl helloRpc = new HelloRpcImpl();
        SimpleRpcServer simpleRpcServer = new SimpleRpcServer(1234);
        simpleRpcServer.registerService(HelloRpc.class, helloRpc);
        simpleRpcServer.start();
    }
}
