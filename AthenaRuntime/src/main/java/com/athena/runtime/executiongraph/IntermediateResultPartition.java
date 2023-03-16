package com.athena.runtime.executiongraph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class IntermediateResultPartition {
    private IntermediateResult totalResult;
    private ExecutionVertex producer;
    private int partitionNumber;

    private static AtomicLong ID = new AtomicLong();
    private Long partitionId;
    private List<List<ExecutionEdge>> consumers = new ArrayList<>();

    public IntermediateResultPartition(IntermediateResult totalResult, ExecutionVertex producer, int partitionNumber) {
        this.totalResult = totalResult;
        this.producer = producer;
        this.partitionNumber = partitionNumber;
        this.partitionId = ID.incrementAndGet();
    }

    public int addConsumerGroup() {
        int pos = consumers.size();
        consumers.add(new ArrayList<>());
        return pos;
    }

    public void addConsumer(ExecutionEdge edge, int consumerIndex) {
        consumers.get(consumerIndex).add(edge);
    }
}
