package com.athena.runtime.stream.paritioner;

import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class ForwardPartitioner<T> extends StreamPartitioner<T> {
    @Override
    public int selectChannel(StreamRecord<T> record) {
        return 0;
    }

    @Override
    public String toString() {
        return "FORWARD";
    }
}
