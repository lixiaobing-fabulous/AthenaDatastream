package com.athena.rpc;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public class HelloRpcImpl implements HelloRpc {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
