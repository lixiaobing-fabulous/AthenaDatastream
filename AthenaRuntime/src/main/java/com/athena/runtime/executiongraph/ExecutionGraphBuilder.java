package com.athena.runtime.executiongraph;

import com.athena.runtime.jobgraph.JobGraph;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class ExecutionGraphBuilder {

    public static ExecutionGraph buildGraph(JobGraph jobGraph) {
        JobInformation jobInformation = new JobInformation(jobGraph.getJobId(), jobGraph.getJobName());
        ExecutionGraph executionGraph = new ExecutionGraph(jobInformation);
        executionGraph.attachJobGraph(jobGraph.getVerticesSortedTopologicallyFromSources());
        return executionGraph;
    }
}
