package com.athena.runtime.stream.paritioner;

import com.athena.runtime.stream.streamrecord.StreamRecord;

import java.util.Random;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class ShufflePartitioner<T> extends StreamPartitioner<T> {
    private Random random = new Random();

    @Override
    public int selectChannel(StreamRecord<T> record) {
        return random.nextInt(numberOfChannels);
    }

    @Override
    public String toString() {
        return "SHUFFLE";
    }
}
