package com.athena.runtime.execute;

import com.athena.runtime.io.network.inputgate.InputGate;
import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.io.network.partition.ResultPartitionManager;
import com.athena.runtime.stream.api.operators.StreamOperator;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class ExecutionEnvironment implements Serializable {
    private StreamOperator operator;
    private ResultPartition[] resultPartitions;
    private InputGate[] inputGates;

    public void setInputGates(InputGate[] inputGates) {
        this.inputGates = inputGates;
    }

    public InputGate[] getInputGates() {
        return inputGates;
    }

    private ResultPartitionManager resultPartitionManager;

    public ResultPartition[] getResultPartitions() {
        return resultPartitions;
    }

    public void setResultPartitions(ResultPartition[] resultPartitions) {
        this.resultPartitions = resultPartitions;
    }

    public ResultPartitionManager getResultPartitionManager() {
        return resultPartitionManager;
    }

    public void setResultPartitionManager(ResultPartitionManager resultPartitionManager) {
        this.resultPartitionManager = resultPartitionManager;
    }

    public StreamOperator getOperator() {
        return operator;
    }

    public void setOperator(StreamOperator operator) {
        this.operator = operator;
    }
}
