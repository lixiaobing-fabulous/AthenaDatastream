package com.athena.runtime.streamgraph;

import com.athena.api.ExecutionConfig;
import com.athena.api.util.tupple.Tuple2;
import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import com.athena.runtime.stream.paritioner.ForwardPartitioner;
import com.athena.runtime.stream.paritioner.RebalancePartitioner;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import com.athena.runtime.stream.task.OneInputStreamTask;
import com.athena.runtime.stream.task.SourceStreamTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamGraph {
    private String jobName;
    private final ExecutionConfig executionConfig;
    private Set<Integer> sources = new HashSet<>();
    private Set<Integer> sinks = new HashSet<>();
    private Map<Integer, StreamNode> streamNodes = new HashMap<>();
    private Map<Integer, Tuple2<Integer, StreamPartitioner<?>>> virtualPartitionNodes = new HashMap<>();

    public Set<Integer> getSourceIds() {
        return sources;
    }

    public StreamGraph setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public StreamGraph(ExecutionConfig executionConfig) {
        this.executionConfig = executionConfig;
    }

    public <IN, OUT> void addOperator(Integer vertexID, StreamOperatorFactory<OUT> operatorFactory, String operatorName) {
        Class<? extends AbstractInvokable> invokableClass =
                operatorFactory.isStreamSource() ? SourceStreamTask.class : OneInputStreamTask.class;
        addOperator(vertexID, operatorFactory, operatorName, invokableClass);
    }

    public StreamNode getStreamNode(Integer vertexID) {
        return streamNodes.get(vertexID);
    }

    public void setParallelism(Integer vertexID, int parallelism) {
        if (getStreamNode(vertexID) != null) {
            getStreamNode(vertexID).setParallelism(parallelism);
        }
    }

    public void setMaxParallelism(int vertexID, int maxParallelism) {
        if (getStreamNode(vertexID) != null) {
            getStreamNode(vertexID).setMaxParallelism(maxParallelism);
        }
    }

    private <OUT> void addOperator(Integer vertexID, StreamOperatorFactory<OUT> operatorFactory, String operatorName,
                                   Class<? extends AbstractInvokable> invokableClass) {
        addNode(vertexID, invokableClass, operatorFactory, operatorName);
    }

    private <OUT> StreamNode addNode(Integer vertexID,
                                     Class<? extends AbstractInvokable> invokableClass,
                                     StreamOperatorFactory<OUT> operatorFactory,
                                     String operatorName) {
        if (streamNodes.containsKey(vertexID)) {
            throw new RuntimeException("Duplicate vertexID " + vertexID);
        }
        StreamNode vertex = new StreamNode(
                vertexID,
                operatorFactory,
                operatorName,
                invokableClass
        );
        streamNodes.put(vertexID, vertex);
        return vertex;
    }

    public void addEdge(int upStreamVertexID, int downStreamVertexID) {
        addEdge(upStreamVertexID, downStreamVertexID, null);
    }

    public void addEdge(Integer upStreamVertexID, int downStreamVertexID, StreamPartitioner<?> partitioner) {
        if (virtualPartitionNodes.containsKey(upStreamVertexID)) {
            int virtualId = upStreamVertexID;
            upStreamVertexID = virtualPartitionNodes.get(virtualId).f0;
            if (partitioner == null) {
                partitioner = virtualPartitionNodes.get(virtualId).f1;
            }
            addEdge(upStreamVertexID, downStreamVertexID, partitioner);
            return;
        }
        StreamNode upstreamNode = getStreamNode(upStreamVertexID);
        StreamNode downstreamNode = getStreamNode(downStreamVertexID);
        if (partitioner == null && upstreamNode.getParallelism() == downstreamNode.getParallelism()) {
            partitioner = new ForwardPartitioner<>();
        } else if (partitioner == null) {
            partitioner = new RebalancePartitioner<>();
        }
        StreamEdge edge = new StreamEdge(upstreamNode, downstreamNode, partitioner);
        getStreamNode(edge.getSourceId()).addOutEdge(edge);
        getStreamNode(edge.getTargetId()).addInEdge(edge);

    }

    public void addLegacySource(int vertexID, StreamOperatorFactory<?> operatorFactory, String operatorName) {
        addOperator(vertexID, operatorFactory, operatorName);
        sources.add(vertexID);
    }

    public void addSink(int vertexID, StreamOperatorFactory<?> operatorFactory, String operatorName) {
        addOperator(vertexID, operatorFactory, operatorName);
        sinks.add(vertexID);
    }

    public <T> void addVirtualPartitionNode(Integer originalId, int virtualId, StreamPartitioner<T> partitioner) {
        virtualPartitionNodes.put(virtualId, new Tuple2<>(originalId, partitioner));
    }

}
