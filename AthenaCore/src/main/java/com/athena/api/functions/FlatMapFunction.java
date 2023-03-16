package com.athena.api.functions;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface FlatMapFunction<T, O> extends Function {
    void flatMap(T value, Collector<O> out) throws Exception;
}
