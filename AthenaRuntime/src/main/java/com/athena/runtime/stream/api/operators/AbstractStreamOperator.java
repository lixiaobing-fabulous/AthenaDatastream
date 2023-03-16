package com.athena.runtime.stream.api.operators;

import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public abstract class AbstractStreamOperator<OUT> implements StreamOperator<OUT> {
    protected transient Output<StreamRecord<OUT>> output;

    public void setOutput(Output<StreamRecord<OUT>> output) {
        this.output = output;
    }
}
