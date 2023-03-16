package com.athena.runtime.stream.api.operators;

import com.athena.api.functions.FilterFunction;
import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamFilter<IN> extends AbstractUdfStreamOperator<IN, FilterFunction<IN>> implements OneInputStreamOperator<IN, IN> {
    public StreamFilter(FilterFunction<IN> userFunction) {
        super(userFunction);
    }

    public void processElement(StreamRecord<IN> element) throws Exception {
        if (userFunction.filter(element.getValue())) {
            output.collect(element);
        }
    }
}
