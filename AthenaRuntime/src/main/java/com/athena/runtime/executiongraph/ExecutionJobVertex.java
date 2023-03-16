package com.athena.runtime.executiongraph;

import com.athena.api.util.KeyGroupUtil;
import com.athena.runtime.jobgraph.IntermediateDataSet;
import com.athena.runtime.jobgraph.JobEdge;
import com.athena.runtime.jobgraph.JobVertex;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class ExecutionJobVertex {
    private ExecutionGraph executionGraph;
    private JobVertex jobVertex;
    private int maxParallelism;
    private int parallelism;
    private ExecutionVertex[] taskVertices;
    private List<IntermediateResult> inputs;
    private IntermediateResult[] producedDataSets;

    public ExecutionJobVertex(ExecutionGraph executionGraph,
                              JobVertex jobVertex) {
        this.executionGraph = executionGraph;
        this.jobVertex = jobVertex;
        int numTaskVertices = jobVertex.getParallelism();
        this.parallelism = numTaskVertices;
        this.maxParallelism = jobVertex.getMaxParallelism() > 0 ? jobVertex.getMaxParallelism() : KeyGroupUtil.computeDefaultMaxParallelism(numTaskVertices);
        this.taskVertices = new ExecutionVertex[numTaskVertices];
        this.inputs = new ArrayList<>(jobVertex.getInputs().size());
        this.producedDataSets = new IntermediateResult[jobVertex.getResults().size()];
        int i = 0;
        for (IntermediateDataSet result : jobVertex.getResults()) {
            this.producedDataSets[i] = new IntermediateResult(
                    result.getId(),
                    this,
                    numTaskVertices
            );
            i++;
        }
        for (int j = 0; j < numTaskVertices; j++) {
            ExecutionVertex vertex = new ExecutionVertex(
                    this,
                    j,
                    producedDataSets
            );
            this.taskVertices[j] = vertex;
        }
    }

    public void connectToPredecessors(Map<Long, IntermediateResult> intermediateDataSets) {
        List<JobEdge> inputs = jobVertex.getInputs();
        int num = 0;
        for (JobEdge edge : inputs) {
            IntermediateResult intermediateResult = intermediateDataSets.get(edge.getSource().getId());
            this.inputs.add(intermediateResult);
            int consumerIndex = intermediateResult.registerConsumer();
            for (int i = 0; i < parallelism; i++) {
                ExecutionVertex executionVertex = taskVertices[i];
                executionVertex.connectSource(num, intermediateResult, edge, consumerIndex);
            }
            num++;
        }
    }
}
