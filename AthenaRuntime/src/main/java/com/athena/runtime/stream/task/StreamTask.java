package com.athena.runtime.stream.task;

import com.athena.runtime.execute.ExecutionEnvironment;
import com.athena.runtime.jobgraph.tasks.AbstractInvokable;
import com.athena.runtime.stream.api.operators.StreamOperator;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamTask extends AbstractInvokable {
    protected StreamOperator operator;

    public StreamTask(ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment);
        operator = executionEnvironment.getOperator();
    }

    public void invoke() {
        try {
            beforeInvoke();
            runTaskLoop();
            afterInvoke();
        } finally {
            cleanUpInvoke();
        }
    }

    protected void cleanUpInvoke() {
    }

    protected void afterInvoke() {
    }

    protected void runTaskLoop() {
        while (true) {
            processInput();
        }
    }

    protected void beforeInvoke() {
    }

    protected void processInput() {
    }
}
