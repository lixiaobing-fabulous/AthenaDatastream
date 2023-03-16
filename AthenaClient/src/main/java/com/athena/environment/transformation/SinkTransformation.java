package com.athena.environment.transformation;

import com.athena.runtime.stream.api.operators.StreamOperatorFactory;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class SinkTransformation<T> extends Transformation<Object> {
    private Transformation<T> input;
    private StreamOperatorFactory<Object> operatorFactory;

    public SinkTransformation(
            Transformation<T> input,
            String name,
            StreamOperatorFactory<Object> operatorFactory,
            int parallelism) {
        super(name, parallelism);
        this.input = input;
        this.operatorFactory = operatorFactory;
    }
    public Transformation<T> getInput() {
        return input;
    }

    public StreamOperatorFactory<T> getOperatorFactory(){
        return (StreamOperatorFactory<T>) operatorFactory;
    }

}
