package com.athena.runtime.jobgraph;

import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class JobVertex {
    private Long vertexId;
    private int parallelism;
    private int maxParallelism;
    private String name;
    private String description;

    private byte[] operatorFactory;
    private static AtomicLong VERTEX_ID = new AtomicLong();
    private final ArrayList<JobEdge> inputs = new ArrayList<>();
    private final ArrayList<IntermediateDataSet> results = new ArrayList<>();

    private String invokableClassName;

    public JobVertex(String name) {
        this.vertexId = VERTEX_ID.incrementAndGet();
        this.name = name;
    }

    public void setInvokableClass(Class<? extends AbstractInvokable> invokable) {
        this.invokableClassName = invokable.getName();
    }


    public JobEdge connectNewDataSetAsInput(JobVertex input, StreamPartitioner<?> partitioner) {
        IntermediateDataSet dataSet = input.createAndAddResultDataSet();
        JobEdge edge = new JobEdge(dataSet, this, partitioner);
        this.inputs.add(edge);
        dataSet.addConsumer(edge);
        return edge;
    }

    public IntermediateDataSet createAndAddResultDataSet() {
        IntermediateDataSet result = new IntermediateDataSet(this);
        this.results.add(result);
        return result;
    }
}
