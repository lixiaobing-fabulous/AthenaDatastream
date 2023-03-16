package com.athena.runtime.jobgraph;

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
public class IntermediateDataSet {
    private Long id;
    private AtomicLong DATASET_ID = new AtomicLong();
    private JobVertex producer;
    private final List<JobEdge> consumers = new ArrayList<>();

    public IntermediateDataSet(JobVertex producer) {
        this.id = DATASET_ID.incrementAndGet();
        this.producer = producer;
    }
    public void addConsumer(JobEdge edge) {
        this.consumers.add(edge);
    }


}
