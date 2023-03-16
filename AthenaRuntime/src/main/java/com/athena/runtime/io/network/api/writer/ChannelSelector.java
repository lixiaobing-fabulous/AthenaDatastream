package com.athena.runtime.io.network.api.writer;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface ChannelSelector<T> {

    void setup(int numberOfChannels);

    int selectChannel(T record);

    boolean isBroadcast();

}
