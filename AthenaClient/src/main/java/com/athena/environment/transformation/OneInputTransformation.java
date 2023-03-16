package com.athena.environment.transformation;

import com.athena.runtime.stream.api.operators.StreamOperatorFactory;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class OneInputTransformation<IN, OUT> extends Transformation<OUT> {
    private Transformation<IN> input;

    private StreamOperatorFactory<OUT> operatorFactory;

    public OneInputTransformation(
            Transformation<IN> input,
            String name,
            StreamOperatorFactory<OUT> operatorFactory,
            int parallelism) {
        super(name, parallelism);
        this.input = input;
        this.operatorFactory = operatorFactory;
    }

    public Transformation<IN> getInput() {
        return input;
    }

    public StreamOperatorFactory<OUT> getOperatorFactory(){
        return operatorFactory;
    }

}
