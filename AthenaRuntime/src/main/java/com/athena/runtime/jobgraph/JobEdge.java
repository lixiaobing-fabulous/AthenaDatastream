package com.athena.runtime.jobgraph;

import com.athena.runtime.stream.paritioner.StreamPartitioner;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
@Setter
public class JobEdge {
    private IntermediateDataSet source;
    private JobVertex target;

    private StreamPartitioner partitioner;

    public JobEdge(IntermediateDataSet source, JobVertex target, StreamPartitioner partitioner) {
        this.source = source;
        this.target = target;
        this.partitioner = partitioner;
    }
}
