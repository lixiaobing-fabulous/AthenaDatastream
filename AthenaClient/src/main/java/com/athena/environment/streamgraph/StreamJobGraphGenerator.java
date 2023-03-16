package com.athena.environment.streamgraph;

import com.athena.runtime.io.network.codec.serializer.JavaSerializer;
import com.athena.runtime.jobgraph.JobEdge;
import com.athena.runtime.jobgraph.JobGraph;
import com.athena.runtime.jobgraph.JobVertex;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import com.athena.runtime.streamgraph.StreamEdge;
import com.athena.runtime.streamgraph.StreamGraph;
import com.athena.runtime.streamgraph.StreamNode;

import java.util.*;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamJobGraphGenerator {
    private StreamGraph streamGraph;
    private JobGraph jobGraph;
    private final Map<Integer, JobVertex> jobVertices = new HashMap<>();
    private final Set<Integer> builtVertices = new HashSet<>();

    public StreamJobGraphGenerator(StreamGraph streamGraph) {
        this.streamGraph = streamGraph;
        this.jobGraph = new JobGraph(streamGraph.getJobName());
    }

    public JobGraph generate() {
        for (Integer sourceId : this.streamGraph.getSourceIds()) {
            createGraph(sourceId);
        }
        return jobGraph;
    }

    private void createGraph(Integer nodeId) {
        if (builtVertices.contains(nodeId)) {
            return;
        }
        StreamNode currentNode = streamGraph.getStreamNode(nodeId);
        createJobVertex(nodeId);
        for (StreamEdge outEdge : currentNode.getOutEdges()) {
            int targetId = outEdge.getTargetId();
            createGraph(targetId);
        }

        for (StreamEdge outEdge : currentNode.getOutEdges()) {
            connectJobEdge(outEdge);
        }
    }

    private void connectJobEdge(StreamEdge streamEdge) {
        int sourceId = streamEdge.getSourceId();
        int targetId = streamEdge.getTargetId();
        JobVertex source = jobVertices.get(sourceId);
        JobVertex target = jobVertices.get(targetId);
        StreamPartitioner<?> partitioner = streamEdge.getOutputPartitioner();
        target.connectNewDataSetAsInput(source, partitioner);

    }

    private JobVertex createJobVertex(Integer streamNodeId) {
        StreamNode streamNode = streamGraph.getStreamNode(streamNodeId);
        JobVertex jobVertex = new JobVertex(streamNode.getOperatorName());
        jobVertex.setInvokableClass(streamNode.getJobVertexClass());
        int parallelism = streamNode.getParallelism();

        if (parallelism > 0) {
            jobVertex.setParallelism(parallelism);
        } else {
            parallelism = jobVertex.getParallelism();
        }
        jobVertex.setMaxParallelism(streamNode.getMaxParallelism());
        StreamOperatorFactory<?> operatorFactory = streamNode.getOperatorFactory();
        byte[] bytes = new JavaSerializer().serialize(operatorFactory);
        jobVertex.setOperatorFactory(bytes);
        jobVertices.put(streamNodeId, jobVertex);
        jobGraph.addVertex(jobVertex);
        return jobVertex;
    }
}
