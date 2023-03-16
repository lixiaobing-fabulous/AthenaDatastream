package com.athena.runtime.stream.api.operators;

import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class TimestampedCollector<T> implements Output<T> {
    private final Output<StreamRecord<T>> output;
    private final StreamRecord<T> reuse;


    public TimestampedCollector(Output<StreamRecord<T>> output) {
        this.output = output;
        this.reuse = new StreamRecord<>(null);
    }

    @Override
    public void collect(T record) {
        output.collect(reuse.replace(record));
    }

    @Override
    public void close() {
        output.close();
    }
}
