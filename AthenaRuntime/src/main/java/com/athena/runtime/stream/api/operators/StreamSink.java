package com.athena.runtime.stream.api.operators;

import com.athena.api.functions.SinkFunction;
import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamSink<IN> extends AbstractUdfStreamOperator<Object, SinkFunction<IN>>
        implements OneInputStreamOperator<IN, Object> {
    public StreamSink(SinkFunction<IN> userFunction) {
        super(userFunction);
    }

    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        userFunction.invoke(element.getValue());
    }
}
