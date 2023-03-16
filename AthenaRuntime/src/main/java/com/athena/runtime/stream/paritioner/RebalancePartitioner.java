package com.athena.runtime.stream.paritioner;

import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class RebalancePartitioner<T> extends StreamPartitioner<T> {
    private int nextChannelToSendTo;

    @Override
    public int selectChannel(StreamRecord<T> record) {
        this.nextChannelToSendTo = (nextChannelToSendTo + 1) % numberOfChannels;
        return nextChannelToSendTo;
    }

    @Override
    public String toString() {
        return "REBALANCE";
    }
}
