package com.athena.environment.datastream;

import com.athena.api.functions.key.KeySelector;
import com.athena.environment.StreamingEnvironment;
import com.athena.environment.transformation.PartitionTransformation;
import com.athena.environment.transformation.Transformation;
import com.athena.runtime.stream.paritioner.HashPartitioner;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class KeyedStream<T, KEY> extends DataStream<T> {
    private KeySelector<T, KEY> keySelector;

    public KeyedStream(StreamingEnvironment environment, Transformation<T> transformation) {
        super(environment, transformation);
    }

    public KeyedStream(DataStream<T> dataStream, KeySelector<T, KEY> keySelector) {
        this(dataStream, keySelector, new PartitionTransformation<>(dataStream.getTransformation(), new HashPartitioner<>(keySelector, 1 << 7)));
    }

    public KeyedStream(
            DataStream<T> stream,
            KeySelector<T, KEY> keySelector,
            PartitionTransformation<T> partitionTransformation) {

        super(stream.getEnvironment(), partitionTransformation);
        this.keySelector = keySelector;
    }

}
