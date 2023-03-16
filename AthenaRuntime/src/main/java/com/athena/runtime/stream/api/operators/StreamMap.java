package com.athena.runtime.stream.api.operators;

import com.athena.api.functions.MapFunction;
import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamMap<IN, OUT> extends AbstractUdfStreamOperator<OUT, MapFunction<IN, OUT>> implements OneInputStreamOperator<IN, OUT> {
    public StreamMap(MapFunction<IN, OUT> mapper) {
        super(mapper);
    }

    public void processElement(StreamRecord<IN> element) throws Exception {
        output.collect(element.replace(userFunction.map(element.getValue())));
    }
}
