package com.athena.environment.datastream;

import com.athena.environment.StreamingEnvironment;
import com.athena.environment.transformation.Transformation;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class DataStreamSource<T> extends DataStream<T> {
    public DataStreamSource(StreamingEnvironment environment, Transformation transformation) {
        super(environment, transformation);
    }
}
