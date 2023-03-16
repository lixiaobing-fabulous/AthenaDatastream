package com.athena.environment.functions;

import com.athena.api.functions.SinkFunction;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class PrintSinkFunction<IN> implements SinkFunction<IN> {
    @Override
    public void invoke(IN value) {
        System.out.println(value);
    }
}
