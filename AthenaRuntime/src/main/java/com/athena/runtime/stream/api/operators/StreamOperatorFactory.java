package com.athena.runtime.stream.api.operators;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface StreamOperatorFactory<OUT> extends Serializable {
    <T extends StreamOperator<OUT>> T createStreamOperator();

    default boolean isStreamSource() {
        return false;
    }


}
