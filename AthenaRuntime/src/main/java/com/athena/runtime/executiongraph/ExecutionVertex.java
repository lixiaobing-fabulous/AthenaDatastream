package com.athena.runtime.executiongraph;

import com.athena.runtime.jobgraph.JobEdge;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class ExecutionVertex {
    private ExecutionJobVertex jobVertex;
    private int subTaskIndex;

    private Map<Long, IntermediateResultPartition> resultPartitions = new HashMap<>();
    private ExecutionEdge[][] inputEdges;

    public ExecutionVertex(ExecutionJobVertex jobVertex, int subTaskIndex, IntermediateResult[] producedDataSets) {
        this.jobVertex = jobVertex;
        this.subTaskIndex = subTaskIndex;
        for (IntermediateResult result : producedDataSets) {
            IntermediateResultPartition irp = new IntermediateResultPartition(result, this, subTaskIndex);
            result.setPartition(subTaskIndex, irp);
            resultPartitions.put(irp.getPartitionId(), irp);
        }
        this.inputEdges = new ExecutionEdge[jobVertex.getJobVertex().getInputs().size()][];


    }

    public void connectSource(int inputNumber, IntermediateResult source, JobEdge edge, int consumerIndex) {
        IntermediateResultPartition[] sourcePartitions = source.getPartitions();
        ExecutionEdge[] edges = connectAllToAll(sourcePartitions, inputNumber, edge.getPartitioner());
        inputEdges[inputNumber] = edges;
        for (ExecutionEdge ee : edges) {
            ee.getSource().addConsumer(ee, consumerIndex);
        }

    }

    private ExecutionEdge[] connectAllToAll(IntermediateResultPartition[] sourcePartitions, int inputNumber, StreamPartitioner partitioner) {
        ExecutionEdge[] edges = new ExecutionEdge[sourcePartitions.length];
        for (int i = 0; i < sourcePartitions.length; i++) {
            IntermediateResultPartition irp = sourcePartitions[i];
            edges[i] = new ExecutionEdge(irp, this, inputNumber, partitioner);
        }

        return edges;
    }
}
