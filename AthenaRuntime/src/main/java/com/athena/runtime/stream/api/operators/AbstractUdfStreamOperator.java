package com.athena.runtime.stream.api.operators;

import com.athena.api.configuration.Configuration;
import com.athena.api.functions.Function;
import com.athena.api.functions.RichFunction;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public abstract class AbstractUdfStreamOperator<OUT, F extends Function> extends AbstractStreamOperator<OUT> {
    protected final F userFunction;

    public AbstractUdfStreamOperator(F userFunction) {
        this.userFunction = userFunction;
    }

    public F getUserFunction() {
        return userFunction;
    }

    public void open() throws Exception {
        if (userFunction instanceof RichFunction) {
            RichFunction richFunction = (RichFunction) userFunction;
            //TODO 获取真正的配置文件
            richFunction.open(new Configuration());
        }

    }

    public void close() throws Exception {
        if (userFunction instanceof RichFunction) {
            RichFunction richFunction = (RichFunction) userFunction;
            richFunction.close();
        }

    }
}
