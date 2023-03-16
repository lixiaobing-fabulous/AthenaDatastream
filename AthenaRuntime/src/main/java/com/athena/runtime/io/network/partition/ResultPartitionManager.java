package com.athena.runtime.io.network.partition;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
public interface ResultPartitionManager extends Serializable {


    void registerResultPartition(ResultPartition partition);

    Object consumeFromPartition(Long partitionId, int subTaskIndex);

}
