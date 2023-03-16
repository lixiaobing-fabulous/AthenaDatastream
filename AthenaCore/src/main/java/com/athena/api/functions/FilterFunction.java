package com.athena.api.functions;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface FilterFunction<T> extends Function {
    boolean filter(T value) throws Exception;

}
