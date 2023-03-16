package com.athena.environment.datastream;

import com.athena.api.functions.FilterFunction;
import com.athena.api.functions.FlatMapFunction;
import com.athena.api.functions.key.KeySelector;
import com.athena.environment.StreamingEnvironment;
import com.athena.environment.functions.PrintSinkFunction;
import com.athena.environment.transformation.OneInputTransformation;
import com.athena.environment.transformation.SinkTransformation;
import com.athena.environment.transformation.Transformation;
import com.athena.runtime.stream.api.operators.*;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class DataStream<T> {
    protected StreamingEnvironment environment;
    protected Transformation<T> transformation;

    public DataStream(StreamingEnvironment environment, Transformation<T> transformation) {
        this.environment = environment;
        this.transformation = transformation;
    }

    public <R> DataStream<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        return transform("Flat Map", new StreamFlatMap<>(flatMapper));
    }

    public DataStream<T> filter(FilterFunction<T> filter) {
        return transform("Filter", new StreamFilter<>(filter));
    }

    public <K> KeyedStream<T, K> keyBy(KeySelector<T, K> key) {
        return new KeyedStream<>(this, key);
    }

    public void print() {
        addSink(new PrintSinkFunction<T>(), "Print to Std. Out");
    }

    private void addSink(PrintSinkFunction<T> printSinkFunction, String sinkName) {
        SinkTransformation<T> sinkTransformation = new SinkTransformation<>(transformation, sinkName,
                SimpleStreamOperatorFactory.of(new StreamSink<>(printSinkFunction)), environment.getParallelism());
        environment.addOperator(sinkTransformation);
    }


    public DataStream<T> setParallelism(int parallelism) {
        transformation.setParallelism(parallelism);
        return this;
    }

    public <R> DataStream<R> transform(String name, OneInputStreamOperator<T, R> operator) {
        return doTransform(name, SimpleStreamOperatorFactory.of(operator));
    }

    private <R> DataStream<R> doTransform(String name, SimpleStreamOperatorFactory<R> operatorFactory) {
        OneInputTransformation<T, R> resultTransformation = new OneInputTransformation<>(this.transformation,
                name,
                operatorFactory,
                environment.getParallelism());
        DataStream<R> result = new DataStream<>(environment, resultTransformation);
        environment.addOperator(resultTransformation);
        return result;
    }

    public StreamingEnvironment getEnvironment() {
        return environment;
    }

    public Transformation<T> getTransformation() {
        return transformation;
    }

}
