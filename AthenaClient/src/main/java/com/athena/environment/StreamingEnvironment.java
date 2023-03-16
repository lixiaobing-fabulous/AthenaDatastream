package com.athena.environment;

import com.athena.api.ExecutionConfig;
import com.athena.api.functions.SourceFunction;
import com.athena.api.util.server.Client;
import com.athena.environment.datastream.DataStreamSource;
import com.athena.environment.functions.FromElementsFunction;
import com.athena.environment.streamgraph.StreamGraphGenerator;
import com.athena.environment.streamgraph.StreamJobGraphGenerator;
import com.athena.environment.transformation.LegacySourceTransformation;
import com.athena.environment.transformation.Transformation;
import com.athena.runtime.executiongraph.*;
import com.athena.runtime.io.network.codec.serializer.JavaSerializer;
import com.athena.runtime.io.network.inputgate.InputChannel;
import com.athena.runtime.io.network.inputgate.InputGate;
import com.athena.runtime.io.network.partition.*;
import com.athena.runtime.jobgraph.JobGraph;
import com.athena.runtime.jobgraph.JobVertex;
import com.athena.runtime.stream.api.operators.SimpleStreamOperatorFactory;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import com.athena.runtime.stream.api.operators.StreamSource;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import com.athena.runtime.streamgraph.StreamGraph;
import com.athena.runtime.taskmanager.Task;
import com.athena.server.ExecutableTask;
import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamingEnvironment {
    private ExecutionConfig config = new ExecutionConfig();
    private Mode mode = Mode.LOCAL;

    enum Mode {
        LOCAL, REMOTE
    }

    protected final List<Transformation<?>> transformations = new ArrayList<>();

    public StreamingEnvironment setRemote() {
        this.mode = Mode.REMOTE;
        return this;
    }

    public StreamingEnvironment setLocal() {
        this.mode = Mode.LOCAL;
        return this;
    }

    public static StreamingEnvironment getRuntimeEnvironment() {
        return new StreamingEnvironment();
    }

    public StreamingEnvironment setParallelism(int parallelism) {
        this.config.setParallelism(parallelism);
        return this;
    }

    public int getParallelism() {
        return this.config.getParallelism();
    }

    public <OUT> DataStreamSource<OUT> fromElements(OUT... data) {
        return fromCollection(Arrays.asList(data));
    }

    private <OUT> DataStreamSource<OUT> fromCollection(Collection<OUT> data) {
        SourceFunction<OUT> function = new FromElementsFunction<OUT>(data);
        return addSource(function, "Collection Source");
    }

    private <OUT> DataStreamSource<OUT> addSource(SourceFunction<OUT> function, String sourceName) {
        return new DataStreamSource<>(this,
                new LegacySourceTransformation(sourceName,
                        SimpleStreamOperatorFactory.of(new StreamSource(function)),
                        getParallelism()));
    }

    public void addOperator(Transformation<?> transformation) {
        this.transformations.add(transformation);
    }

    public void execute(String jobName) {
        execute(getStreamGraph(jobName));
    }

    public void execute(StreamGraph streamGraph) {
        JobGraph jobGraph = new StreamJobGraphGenerator(streamGraph).generate();
        ExecutionGraph executionGraph = ExecutionGraphBuilder.buildGraph(jobGraph);
        deploy(executionGraph);
    }

    private void deploy(ExecutionGraph executionGraph) {
        ResultPartitionManager resultPartitionManager;
        if (mode == Mode.LOCAL) {
            resultPartitionManager = new LocalResultPartitionManager();
        } else {
            resultPartitionManager = new RemoteResultPartitionManager();
        }

        Map<Long, ExecutionJobVertex> tasks = executionGraph.getTasks();
        int count = 0;
        for (ExecutionJobVertex executionJobVertex : tasks.values()) {
            count += executionJobVertex.getTaskVertices().length;
            deployExecutionJob(executionJobVertex, resultPartitionManager);
        }
        System.out.println(count);
    }

    @SneakyThrows
    private void deployExecutionJob(ExecutionJobVertex executionJobVertex, ResultPartitionManager resultPartitionManager) {
        ExecutionVertex[] taskVertices = executionJobVertex.getTaskVertices();
        for (ExecutionVertex taskVertex : taskVertices) {
            JobVertex jobVertex = executionJobVertex.getJobVertex();
            String invokableClassName = jobVertex.getInvokableClassName();
            ExecutionEdge[][] inputEdges = taskVertex.getInputEdges();
            Map<Long, IntermediateResultPartition> resultIntermediatePartitions = taskVertex.getResultPartitions();
            byte[] operatorFactory = jobVertex.getOperatorFactory();
            StreamOperatorFactory operator = (StreamOperatorFactory) new JavaSerializer().deserialize(operatorFactory);

            InputGate[] inputGates = createInputGates(taskVertex, inputEdges);
            ResultPartition[] resultPartitions = createResultPartitions(resultPartitionManager, resultIntermediatePartitions);
            String jobTaskName = jobVertex.getName();
            Task task = new Task(jobTaskName, invokableClassName, operator, resultPartitionManager, inputGates, resultPartitions, taskVertex.getSubTaskIndex());
            if (mode == Mode.LOCAL) {
                new Thread(task).start();
            } else if (mode == Mode.REMOTE) {
                com.athena.server.JavaSerializer javaSerializer = new com.athena.server.JavaSerializer();
                javaSerializer.serialize(task);
                ClassLoader classLoader = this.getClass().getClassLoader();
                URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
                URL[] urLs = urlClassLoader.getURLs();
                new Client("localhost", 6666).write(new ExecutableTask(javaSerializer.serialize(task), urLs));
            } else {
                throw new UnsupportedOperationException(mode.name());
            }
        }
    }

    private ResultPartition[] createResultPartitions(ResultPartitionManager resultPartitionManager, Map<Long, IntermediateResultPartition> resultPartitionMap) {
        ResultPartition[] resultPartitions = new ResultPartition[resultPartitionMap.size()];
        IntermediateResultPartition[] intermediateResultPartitions = resultPartitionMap.values().toArray(new IntermediateResultPartition[0]);
        for (int i = 0; i < resultPartitionMap.size(); i++) {
            IntermediateResultPartition partition = intermediateResultPartitions[i];
            Long partitionId = partition.getPartitionId();
            List<ExecutionEdge> executionEdges = partition.getConsumers().get(0);
            ResultSubpartition[] resultSubpartitions = new ResultSubpartition[executionEdges.size()];
            ExecutionEdge executionEdge = executionEdges.get(0);
            StreamPartitioner partitioner = executionEdge.getPartitioner();
            partitioner.setup(executionEdges.size());
            resultPartitions[i] = new ResultPartition(partitionId, resultSubpartitions, resultPartitionManager, partitioner);
        }
        return resultPartitions;
    }

    private static InputGate[] createInputGates(ExecutionVertex taskVertex, ExecutionEdge[][] inputEdges) {
        InputGate[] inputGates = new InputGate[inputEdges.length];
        for (int i = 0; i < inputEdges.length; i++) {
            ExecutionEdge[] inputEdge = inputEdges[i];
            InputChannel[] inputChannels = new InputChannel[inputEdge.length];
            for (int j = 0; j < inputEdge.length; j++) {
                ExecutionEdge executionEdge = inputEdge[j];
                IntermediateResultPartition source = executionEdge.getSource();
                Long partitionId = source.getPartitionId();
                inputChannels[j] = new InputChannel(partitionId, taskVertex.getSubTaskIndex());
            }
            inputGates[i] = new InputGate(inputChannels);
        }
        return inputGates;
    }


    private StreamGraph getStreamGraph(String jobName) {
        return new StreamGraphGenerator(transformations, config).setJobName(jobName).generate();
    }
}
