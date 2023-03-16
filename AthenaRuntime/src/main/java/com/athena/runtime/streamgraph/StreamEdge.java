package com.athena.runtime.streamgraph;

import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class StreamEdge {
    private int sourceId;
    private int targetId;
    private StreamPartitioner<?> outputPartitioner;

    public StreamEdge(StreamNode sourceNode, StreamNode targetNode, StreamPartitioner<?> outputPartitioner) {
        this.sourceId = sourceNode.getId();
        this.targetId = targetNode.getId();
        this.outputPartitioner = outputPartitioner;
    }
}
