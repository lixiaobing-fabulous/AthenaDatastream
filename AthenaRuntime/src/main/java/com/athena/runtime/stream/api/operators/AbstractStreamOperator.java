package com.athena.runtime.stream.api.operators;

import com.athena.api.functions.key.KeySelector;
import com.athena.runtime.stream.streamrecord.StreamRecord;

import java.util.Objects;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public abstract class AbstractStreamOperator<OUT> implements StreamOperator<OUT> {
    protected transient Output<StreamRecord<OUT>> output;
    private transient KeySelector<?, ?> stateKeySelector;
    private transient StreamOperatorStateHandler stateHandler;

    public void setOutput(Output<StreamRecord<OUT>> output) {
        this.output = output;
    }

    @Override
    public void setKeyContextElement(StreamRecord record) throws Exception {
        setKeyContextElement(record, stateKeySelector);
    }

    private <T> void setKeyContextElement(StreamRecord<T> record, KeySelector<T, ?> keySelector) throws Exception {
        if (Objects.nonNull(keySelector)) {
            Object key = keySelector.getKey(record.getValue());
            setCurrentKey(key);
        }
    }

    public void setCurrentKey(Object key) {
        stateHandler.setCurrentKey(key);
    }

    public Object getCurrentKey() {
        return stateHandler.getCurrentKey();
    }
}
