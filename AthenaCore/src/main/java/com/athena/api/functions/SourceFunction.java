package com.athena.api.functions;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface SourceFunction<T> extends Function {

    void run(Collector<T> collector) throws Exception;

    interface SourceContext<T> {
        void collect(T element);
    }
}
