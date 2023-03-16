package com.athena.runtime.stream.task;

import com.athena.runtime.io.network.inputgate.InputChannel;
import com.athena.runtime.io.network.inputgate.InputGate;
import com.athena.runtime.io.network.partition.ResultPartition;
import com.athena.runtime.io.network.partition.ResultPartitionManager;
import com.athena.runtime.io.network.partition.ResultSubpartition;
import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class DefaultInput<INPUT> implements Input{
    private InputGate[] inputGates;
    private int curIdx = 0;

    private BlockingQueue<INPUT> buffer;
    private ResultPartitionManager resultPartitionManager;

    public DefaultInput(InputGate[] inputGates, ResultPartitionManager resultPartitionManager) {
        this.inputGates = inputGates;
        this.resultPartitionManager = resultPartitionManager;
        this.buffer = new ArrayBlockingQueue<>(100);
        for (InputGate inputGate : inputGates) {
            for (InputChannel inputChannel : inputGate.getInputChannels()) {
                new Thread(new InputChannelReader(inputChannel, resultPartitionManager, buffer)).start();
            }
        }
    }

    @Override
    @SneakyThrows
    public INPUT getElement() {
        return buffer.take();
    }

    private static class InputChannelReader implements Runnable {
        private ResultSubpartition subpartition;
        private BlockingQueue buffer;

        public InputChannelReader(InputChannel inputChannel, ResultPartitionManager resultPartitionManager, BlockingQueue buffer) {
            Long partitionId = inputChannel.getPartitionId();
            int subTaskIndex = inputChannel.getSubTaskIndex();
            ResultPartition resultPartition = resultPartitionManager.getRegisteredPartitions().get(partitionId);
            this.subpartition = resultPartition.getSubpartitions()[subTaskIndex];
            this.buffer = buffer;
        }


        @Override
        @SneakyThrows
        public void run() {
            while (true) {
                buffer.put(subpartition.consume());
            }
        }
    }

}
