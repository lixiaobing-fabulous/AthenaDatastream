package com.athena;

import com.athena.worker.StandaloneWorker;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class StandaloneWorker1 extends StandaloneWorker {
    public static void main(String[] args) {
        start(8888);
    }
}
