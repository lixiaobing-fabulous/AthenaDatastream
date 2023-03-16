package com.athena.runtime.stream.api.operators;

import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface OneInputStreamOperator<IN, OUT> extends StreamOperator<OUT> {
    void processElement(StreamRecord<IN> element) throws Exception;

}
