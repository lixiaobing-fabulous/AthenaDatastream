package com.athena.runtime.io.network.partition;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/16
 * @Version 1.0
 **/
public interface RemoteConsumePartition {
    Object consume(int subIndex);
}
