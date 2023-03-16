package com.athena.runtime.stream.api.operators;

import com.athena.api.functions.SourceFunction;
import com.athena.runtime.execute.ExecutionEnvironment;
import com.athena.runtime.stream.task.StreamTask;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamSource<OUT, SRC extends SourceFunction<OUT>> extends AbstractUdfStreamOperator<OUT, SRC> {
    private transient TimestampedCollector<OUT> collector;

    public StreamSource(SRC sourceFunction) {
        super(sourceFunction);
    }

    @Override
    public void open() throws Exception {
        collector = new TimestampedCollector(output);
    }

    public void run() throws Exception {
        userFunction.run(collector);
    }

}
