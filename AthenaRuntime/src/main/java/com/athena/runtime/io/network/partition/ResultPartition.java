package com.athena.runtime.io.network.partition;

import com.athena.runtime.stream.paritioner.StreamPartitioner;
import com.athena.runtime.stream.streamrecord.StreamRecord;
import lombok.Getter;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
@Getter
public class ResultPartition<OUT> implements Serializable {
    protected Long partitionId;

    protected ResultSubpartition<OUT>[] subpartitions;

    protected ResultPartitionManager resultPartitionManager;
    protected StreamPartitioner partitioner;

    public ResultPartition(Long partitionId, ResultSubpartition[] subpartitions, ResultPartitionManager resultPartitionManager, StreamPartitioner partitioner) {
        this.partitionId = partitionId;
        this.subpartitions = subpartitions;
        for (int i = 0; i < subpartitions.length; i++) {
            subpartitions[i] = new ResultSubpartition(this);
        }
        this.resultPartitionManager = resultPartitionManager;
        this.partitioner = partitioner;
        resultPartitionManager.registerResultPartition(this);
    }

    public void write(StreamRecord<OUT> value) {
        int index = partitioner.selectChannel(value);
        subpartitions[index].write(value.getValue());
    }
}
