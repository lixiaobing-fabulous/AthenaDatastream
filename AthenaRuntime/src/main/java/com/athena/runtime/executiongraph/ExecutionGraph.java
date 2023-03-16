package com.athena.runtime.executiongraph;

import com.athena.runtime.jobgraph.JobVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class ExecutionGraph {
    private JobInformation jobInformation;
    private Map<Long, IntermediateResult> intermediateResults = new HashMap<>();
    private Map<Long, ExecutionJobVertex> tasks = new HashMap<>();

    public ExecutionGraph(JobInformation jobInformation) {
        this.jobInformation = jobInformation;
    }

    public void attachJobGraph(Collection<JobVertex> sortedJobVertices) {
        for (JobVertex jobVertex : sortedJobVertices) {
            ExecutionJobVertex executionJobVertex = new ExecutionJobVertex(this, jobVertex);
            executionJobVertex.connectToPredecessors(this.intermediateResults);
            tasks.putIfAbsent(jobVertex.getVertexId(), executionJobVertex);
            for (IntermediateResult res : executionJobVertex.getProducedDataSets()) {
                intermediateResults.put(res.getId(), res);
            }
        }
    }
    public Map<Long, ExecutionJobVertex> getTasks(){
        return tasks;
    }
}
