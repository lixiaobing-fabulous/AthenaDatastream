package com.athena.runtime.taskmanager;

import com.athena.runtime.execute.ExecutionEnvironment;
import com.athena.runtime.executiongraph.ExecutionEdge;
import com.athena.runtime.executiongraph.IntermediateResultPartition;
import com.athena.runtime.io.network.inputgate.InputChannel;
import com.athena.runtime.io.network.inputgate.InputGate;
import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.io.network.partition.ResultPartitionManager;
import com.athena.runtime.io.network.partition.ResultSubpartition;
import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.api.operators.Output;
import com.athena.runtime.stream.api.operators.StreamOperator;
import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import com.athena.runtime.stream.paritioner.StreamPartitioner;
import com.athena.runtime.stream.streamrecord.StreamRecord;
import com.athena.runtime.stream.task.DefaultInput;
import com.athena.runtime.stream.task.DefaultOutput;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class Task implements Runnable, Serializable {
    private String invokableClassName = "";
    private ExecutionEnvironment environment;
    private StreamOperatorFactory operatorFactory;
    private int subTaskIndex;
    private String jobTaskName;
    private ResultPartition[] resultPartitions;


    public Task(String jobTaskName, String invokableClassName, StreamOperatorFactory operatorFactory, ResultPartitionManager resultPartitionManager, InputGate[] inputGates, ResultPartition[] resultPartitions, int subTaskIndex) {
        this.jobTaskName = jobTaskName;
        this.invokableClassName = invokableClassName;
        this.operatorFactory = operatorFactory;
        this.subTaskIndex = subTaskIndex;
        this.environment = new ExecutionEnvironment();
        this.resultPartitions = resultPartitions;
        environment.setOperator(operatorFactory.createStreamOperator());
        environment.setResultPartitionManager(resultPartitionManager);
        environment.setResultPartitions(resultPartitions);
        environment.setInputGates(inputGates);

    }


    public void run() {
        doRun();
    }

    @SneakyThrows
    private void doRun() {
        for (ResultPartition resultPartition : this.resultPartitions) {
            resultPartition.register2PartitionManager();
        }
        Class<? extends AbstractInvokable> invokableClazz = Class.forName(invokableClassName).asSubclass(AbstractInvokable.class);
        Constructor<? extends AbstractInvokable> constructor = invokableClazz.getConstructor(ExecutionEnvironment.class);

        AbstractInvokable invokable = constructor.newInstance(environment);
        invokable.invoke();
    }
}
