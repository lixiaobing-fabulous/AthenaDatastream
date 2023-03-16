package com.athena.runtime.executiongraph;

import lombok.Data;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class IntermediateResult {
    private Long id;
    private ExecutionJobVertex producer;
    private int numParallelProducers;
    private IntermediateResultPartition[] partitions;
    private int numConsumers;

    public IntermediateResult(Long id, ExecutionJobVertex producer, int numParallelProducers) {
        this.id = id;
        this.producer = producer;
        this.numParallelProducers = numParallelProducers;
        this.partitions = new IntermediateResultPartition[numParallelProducers];
    }

    public void setPartition(int partitionNumber, IntermediateResultPartition partition) {
        this.partitions[partitionNumber] = partition;
    }

    public int registerConsumer() {
        final int index = numConsumers;
        numConsumers++;
        for (IntermediateResultPartition partition : partitions) {
            partition.addConsumerGroup();
        }
        return index;
    }
}
