package com.athena.runtime.stream.api.operators;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class SimpleStreamOperatorFactory<OUT> implements StreamOperatorFactory<OUT> {
    private final StreamOperator<OUT> operator;

    public SimpleStreamOperatorFactory(StreamOperator<OUT> operator) {
        this.operator = operator;
    }

    public static <OUT> SimpleStreamOperatorFactory<OUT> of(StreamOperator<OUT> operator) {
        return new SimpleStreamOperatorFactory<OUT>(operator);
    }

    @Override
    public <T extends StreamOperator<OUT>> T createStreamOperator() {
        return (T) operator;
    }

    @Override
    public boolean isStreamSource() {
        return operator instanceof StreamSource;
    }

}
