package com.athena.runtime.stream.task;

import com.athena.runtime.execute.ExecutionEnvironment;
import com.athena.runtime.io.network.inputgate.InputGate;
import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.stream.api.operators.AbstractStreamOperator;
import com.athena.runtime.stream.api.operators.OneInputStreamOperator;
import com.athena.runtime.stream.streamrecord.StreamRecord;
import lombok.SneakyThrows;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class OneInputStreamTask extends StreamTask {
    private OneInputStreamOperator oneInputStreamOperator;
    private ResultPartition[] resultPartitions;
    private Input input;

    @SneakyThrows
    public OneInputStreamTask(ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment);
        oneInputStreamOperator = (OneInputStreamOperator) executionEnvironment.getOperator();
        this.resultPartitions = executionEnvironment.getResultPartitions();
        InputGate[] inputGates = executionEnvironment.getInputGates();
        input = new DefaultInput(inputGates, executionEnvironment.getResultPartitionManager());
        if (oneInputStreamOperator instanceof AbstractStreamOperator) {
            ((AbstractStreamOperator<?>) oneInputStreamOperator).setOutput(new DefaultOutput(this.resultPartitions));
        }
        oneInputStreamOperator.open();
    }

    @Override
    @SneakyThrows
    protected void processInput() {
        oneInputStreamOperator.processElement(new StreamRecord(input.getElement()));
    }
}
