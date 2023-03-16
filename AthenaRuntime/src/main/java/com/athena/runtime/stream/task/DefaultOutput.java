package com.athena.runtime.stream.task;

import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.stream.api.operators.Output;
import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
public class DefaultOutput<OUT> implements Output<StreamRecord<OUT>> {
    private ResultPartition[] resultPartitions;

    public DefaultOutput(ResultPartition[] resultPartitions) {
        this.resultPartitions = resultPartitions;
    }

    @Override
    public void collect(StreamRecord<OUT> record) {
        for (ResultPartition resultPartition : resultPartitions) {
            resultPartition.write(record);
        }
    }

    @Override
    public void close() {

    }
}
