package com.athena.runtime.io.network.partition;

import java.util.Map;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/15
 * @Version 1.0
 **/
public class RemoteResultPartitionManager implements ResultPartitionManager {
    @Override
    public void registerResultPartition(ResultPartition partition) {

    }

    @Override
    public Map<Long, ResultPartition> getRegisteredPartitions() {
        return null;
    }
}
