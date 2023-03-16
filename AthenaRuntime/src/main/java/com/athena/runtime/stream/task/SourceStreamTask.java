package com.athena.runtime.stream.task;

import com.athena.runtime.execute.ExecutionEnvironment;
import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.stream.api.operators.AbstractStreamOperator;
import com.athena.runtime.stream.api.operators.StreamSource;
import lombok.SneakyThrows;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class SourceStreamTask extends StreamTask {
    private StreamSource streamSource;
    private ResultPartition[] resultPartitions;

    @SneakyThrows
    public SourceStreamTask(ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment);
        streamSource = (StreamSource) executionEnvironment.getOperator();
        this.resultPartitions = executionEnvironment.getResultPartitions();
        if (streamSource instanceof AbstractStreamOperator) {
            ((AbstractStreamOperator<?>) streamSource).setOutput(new DefaultOutput(this.resultPartitions));
        }

        streamSource.open();
    }

    @Override
    @SneakyThrows
    protected void runTaskLoop() {
        streamSource.run();
    }
}
