package com.athena.runtime.io.network.partition;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
public class ResultSubpartition<OUT> implements Serializable {
    private ResultPartition parent;
    private BlockingQueue<OUT> buffer;

    public ResultSubpartition(ResultPartition parent) {
        this.parent = parent;
        this.buffer = new ArrayBlockingQueue<>(100);
    }

    public void write(OUT value) {
        this.buffer.add(value);
    }

    @SneakyThrows
    public OUT consume() {
        return this.buffer.take();
    }

}
