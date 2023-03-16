package com.athena.runtime.executiongraph;

import com.athena.runtime.stream.paritioner.StreamPartitioner;
import lombok.Getter;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Getter
public class ExecutionEdge {
    private final IntermediateResultPartition source;

    private final ExecutionVertex target;

    private final int inputNum;

    private StreamPartitioner partitioner;

    public ExecutionEdge(IntermediateResultPartition source, ExecutionVertex target, int inputNum, StreamPartitioner partitioner) {
        this.source = source;
        this.target = target;
        this.inputNum = inputNum;
        this.partitioner = partitioner;
    }

    public IntermediateResultPartition getSource() {
        return source;
    }

    public ExecutionVertex getTarget() {
        return target;
    }

    public int getInputNum() {
        return inputNum;
    }

    @Override
    public String toString() {
        return "ExecutionEdge [" + source + " <=> " + target + "]";
    }

}
