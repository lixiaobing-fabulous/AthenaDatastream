package com.athena.api.functions;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface Partitioner<K> extends Function {
    int partition(K key, int numPartitions);

}
