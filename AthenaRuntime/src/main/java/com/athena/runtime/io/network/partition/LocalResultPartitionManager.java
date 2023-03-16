package com.athena.runtime.io.network.partition;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/15
 * @Version 1.0
 **/
public class LocalResultPartitionManager implements ResultPartitionManager {
    private Map<Long, ResultPartition> registeredPartitions = new HashMap<>();

    public void registerResultPartition(ResultPartition partition) {
        registeredPartitions.put(partition.getPartitionId(), partition);
    }

    public Map<Long, ResultPartition> getRegisteredPartitions() {
        return registeredPartitions;
    }

}
