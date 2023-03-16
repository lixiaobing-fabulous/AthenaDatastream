package com.athena.runtime.jobgraph.tasks;

import com.athena.runtime.execute.ExecutionEnvironment;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public abstract class AbstractInvokable {
    protected ExecutionEnvironment executionEnvironment;

    public AbstractInvokable(ExecutionEnvironment executionEnvironment) {
        this.executionEnvironment = executionEnvironment;
    }

    public abstract void invoke();
}
