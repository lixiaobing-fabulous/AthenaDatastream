package com.athena.environment.streamgraph;

import com.athena.api.ExecutionConfig;
import com.athena.environment.transformation.*;
import com.athena.runtime.stream.api.operators.OneInputStreamOperator;
import com.athena.runtime.streamgraph.StreamGraph;

import java.util.*;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamGraphGenerator {
    private final List<Transformation<?>> transformations;
    private String jobName;
    private final ExecutionConfig executionConfig;
    private StreamGraph streamGraph;
    private Map<Transformation<?>, Collection<Integer>> alreadyTransformed;

    public StreamGraphGenerator(List<Transformation<?>> transformations, ExecutionConfig executionConfig) {
        this.transformations = transformations;
        this.executionConfig = executionConfig;
    }
    public StreamGraphGenerator setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public StreamGraph generate() {
        streamGraph = new StreamGraph(executionConfig);
        streamGraph.setJobName(jobName);
        alreadyTransformed = new HashMap<>();
        for (Transformation<?> transformation : transformations) {
            transform(transformation);
        }
        return streamGraph;
    }

    private Collection<Integer> transform(Transformation<?> transformation) {
        if (alreadyTransformed.containsKey(transformation)) {
            return alreadyTransformed.get(transformation);
        }
        Collection<Integer> transformedIds;
        if (transformation instanceof OneInputTransformation) {
            transformedIds = transformOneInputTransform((OneInputTransformation<?, ?>) transformation);
        } else if (transformation instanceof LegacySourceTransformation) {
            transformedIds = transformLegacySource((LegacySourceTransformation<?>) transformation);
        } else if (transformation instanceof SinkTransformation<?>) {
            transformedIds = transformSink((SinkTransformation<?>) transformation);
        } else if (transformation instanceof PartitionTransformation<?>) {
            transformedIds = transformPartition((PartitionTransformation<?>) transformation);
        } else {
            throw new IllegalStateException("Unknown transformation: " + transformation);
        }
        if (!alreadyTransformed.containsKey(transformation)) {
            alreadyTransformed.put(transformation, transformedIds);
        }
        return transformedIds;
    }

    private <T> Collection<Integer> transformPartition(PartitionTransformation<T> partition) {
        Transformation<T> input = partition.getInput();
        Collection<Integer> transformedIds = transform(input);
        List<Integer> resultIds = new ArrayList<>();

        for (Integer transformedId : transformedIds) {
            int virtualId = Transformation.getNewNodeId();
            streamGraph.addVirtualPartitionNode(
                    transformedId, virtualId, partition.getPartitioner());
            resultIds.add(virtualId);
        }
        return resultIds;
    }

    private Collection<Integer> transformSink(SinkTransformation<?> sink) {
        Collection<Integer> inputIds = transform(sink.getInput());


        streamGraph.addSink(sink.getId(),
                sink.getOperatorFactory(),
                "Sink: " + sink.getName());

        int parallelism = sink.getParallelism() != -1 ?
                sink.getParallelism() : executionConfig.getParallelism();
        streamGraph.setParallelism(sink.getId(), parallelism);
        streamGraph.setMaxParallelism(sink.getId(), sink.getMaxParallelism());

        for (Integer inputId : inputIds) {
            streamGraph.addEdge(inputId,
                    sink.getId()
            );
        }

        return Collections.emptyList();

    }

    private Collection<Integer> transformLegacySource(LegacySourceTransformation<?> source) {
        streamGraph.addLegacySource(source.getId(),
                source.getOperatorFactory(),
                "Source: " + source.getName());
        int parallelism = source.getParallelism() != -1 ?
                source.getParallelism() : executionConfig.getParallelism();
        streamGraph.setParallelism(source.getId(), parallelism);
        streamGraph.setMaxParallelism(source.getId(), source.getMaxParallelism());
        return Collections.singleton(source.getId());
    }

    private Collection<Integer> transformOneInputTransform(OneInputTransformation<?, ?> transform) {
        Collection<Integer> inputIds = transform(transform.getInput());
        if (alreadyTransformed.containsKey(transform)) {
            return alreadyTransformed.get(transform);
        }
        streamGraph.addOperator(transform.getId(),
                transform.getOperatorFactory(),
                transform.getName());
        int parallelism = transform.getParallelism() != -1 ?
                transform.getParallelism() : executionConfig.getParallelism();
        streamGraph.setParallelism(transform.getId(), parallelism);
        streamGraph.setMaxParallelism(transform.getId(), transform.getMaxParallelism());

        for (Integer inputId : inputIds) {
            streamGraph.addEdge(inputId, transform.getId());
        }
        return Collections.singleton(transform.getId());
    }
}
