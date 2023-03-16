package com.athena.environment.transformation;

import com.athena.runtime.stream.paritioner.StreamPartitioner;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class PartitionTransformation<T> extends Transformation<T> {
    private Transformation<T> input;
    private StreamPartitioner<T> partitioner;

    public PartitionTransformation(
            Transformation<T> input,
            StreamPartitioner<T> partitioner) {
        super("Partition", input.getParallelism());
        this.input = input;
        this.partitioner = partitioner;
    }

    public Transformation<T> getInput() {
        return input;
    }

    public StreamPartitioner<T> getPartitioner() {
        return partitioner;
    }

}
