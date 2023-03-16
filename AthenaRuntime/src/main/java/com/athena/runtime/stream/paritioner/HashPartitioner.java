package com.athena.runtime.stream.paritioner;

import com.athena.api.functions.key.KeySelector;
import com.athena.api.util.KeyGroupUtil;
import com.athena.runtime.stream.streamrecord.StreamRecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class HashPartitioner<T, K> extends StreamPartitioner<T> {
    private KeySelector<T, K> keySelector;
    private int maxParrelellism;

    public HashPartitioner(KeySelector<T, K> keySelector, int maxParrelellism) {
        this.keySelector = keySelector;
        this.maxParrelellism = maxParrelellism;
    }

    @Override
    public int selectChannel(StreamRecord<T> record) {
        K key;
        try {
            key = keySelector.getKey(record.getValue());
        } catch (Exception e) {
            throw new RuntimeException("Could not extract key from " + record.getValue(), e);
        }
        return KeyGroupUtil.assignKeyToParallelOperator(key, maxParrelellism, numberOfChannels);
    }

    @Override
    public String toString() {
        return "HASH";
    }
}
