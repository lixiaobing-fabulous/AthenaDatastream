package com.athena.runtime.streamgraph;

import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
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
public class StreamNode {
    private final int id;

    private final String operatorName;

    private int parallelism;
    private int maxParallelism;
    private List<StreamEdge> outEdges = new ArrayList<>();
    private List<StreamEdge> inEdges = new ArrayList<>();

    private Class<? extends AbstractInvokable> jobVertexClass;
    private transient StreamOperatorFactory<?> operatorFactory;

    public StreamNode(int id,
                      StreamOperatorFactory<?> operatorFactory,
                      String operatorName,
                      Class<? extends AbstractInvokable> jobVertexClass) {
        this.id = id;
        this.operatorName = operatorName;
        this.jobVertexClass = jobVertexClass;
        this.operatorFactory = operatorFactory;
    }

    public void addOutEdge(StreamEdge outEdge) {
        if (outEdge.getSourceId() != getId()) {
            throw new IllegalArgumentException("Source id doesn't match the StreamNode id");
        } else {
            outEdges.add(outEdge);
        }

    }

    public void addInEdge(StreamEdge inEdge) {
        if (inEdge.getTargetId() != getId()) {
            throw new IllegalArgumentException("Destination id doesn't match the StreamNode id");
        } else {
            inEdges.add(inEdge);
        }

    }
}
