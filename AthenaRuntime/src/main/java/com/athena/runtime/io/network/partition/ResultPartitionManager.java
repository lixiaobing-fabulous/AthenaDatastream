package com.athena.runtime.io.network.partition;

import com.athena.api.util.server.Server;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
public interface ResultPartitionManager extends Serializable {


    void registerResultPartition(ResultPartition partition);

    Map<Long, ResultPartition> getRegisteredPartitions();
}
