package com.athena.environment.functions;

import com.athena.api.functions.Collector;
import com.athena.api.functions.SourceFunction;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class FromElementsFunction<T> implements SourceFunction<T> {
    private Iterable<T> elements;

    public FromElementsFunction(Iterable<T> elements) {
        this.elements = elements;
    }


    @Override
    public void run(Collector<T> collector) throws Exception {
        for (T element : elements) {
            collector.collect(element);
        }

    }
}
