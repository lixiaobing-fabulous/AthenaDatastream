package com.athena.runtime.jobgraph;

import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class JobGraph {
    private Long jobId;
    private String jobName;
    private static AtomicLong JOB_ID = new AtomicLong();
    private final Map<Long, JobVertex> taskVertices = new LinkedHashMap<>();

    public JobGraph(String jobName) {
        this.jobId = JOB_ID.incrementAndGet();
        this.jobName = jobName;
    }

    public void addVertex(JobVertex jobVertex) {
        taskVertices.put(jobVertex.getVertexId(), jobVertex);
    }

    public List<JobVertex> getVerticesSortedTopologicallyFromSources() {

        // Kahn's algorithm https://en.wikipedia.org/wiki/Topological_sorting
        if (this.taskVertices.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobVertex> sorted = new ArrayList<>(this.taskVertices.size());
        Set<JobVertex> remaining = new LinkedHashSet<>(this.taskVertices.values());
        Iterator<JobVertex> iterator = remaining.iterator();
        while (iterator.hasNext()) {
            JobVertex vertex = iterator.next();
            if (vertex.getInputs().isEmpty()) {
                sorted.add(vertex);
                iterator.remove();
            }
        }
        int startNodePos = 0;
        while (!remaining.isEmpty()) {
            JobVertex current = sorted.get(startNodePos++);
            addNodesThatHaveNoNewPredecessors(current, sorted, remaining);
        }

        return sorted;
    }

    private void addNodesThatHaveNoNewPredecessors(JobVertex start, List<JobVertex> target, Set<JobVertex> remaining) {
        for (IntermediateDataSet result : start.getResults()) {
            for (JobEdge edge : result.getConsumers()) {
                JobVertex v = edge.getTarget();
                boolean hasNewPredecessors = false;
                for (JobEdge e : v.getInputs()) {
                    if (!target.contains(e.getSource().getProducer())) {
                        hasNewPredecessors = true;
                        break;
                    }
                }
                if (!hasNewPredecessors) {
                    target.add(v);
                    remaining.remove(v);
                    addNodesThatHaveNoNewPredecessors(v, target, remaining);
                }
            }
        }
    }
}
