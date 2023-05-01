package com.athena.runtime.stream.api.operators;

import com.athena.runtime.stream.streamrecord.StreamRecord;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface StreamOperator<OUT> extends Serializable {
    void open() throws Exception;

    void close() throws Exception;

    void setKeyContextElement(StreamRecord<?> record) throws Exception;

}
