package com.athena.api.functions;

import com.athena.api.configuration.Configuration;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface RichFunction extends Function {

    void open(Configuration parameters) throws Exception;

    void close() throws Exception;

    RuntimeContext getRuntimeContext();

    void setRuntimeContext(RuntimeContext t);
}
