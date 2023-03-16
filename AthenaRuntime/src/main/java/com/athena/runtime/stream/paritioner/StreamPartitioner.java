package com.athena.runtime.stream.paritioner;

import com.athena.runtime.io.network.api.writer.ChannelSelector;
import com.athena.runtime.stream.streamrecord.StreamRecord;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public abstract class StreamPartitioner<T> implements ChannelSelector<StreamRecord<T>>, Serializable {
    protected int numberOfChannels;

    @Override
    public void setup(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public boolean isBroadcast() {
        return false;
    }
}
