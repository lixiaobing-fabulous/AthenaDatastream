package com.athena.runtime.io.network.inputgate;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/12
 * @Version 1.0
 **/
@Getter
public class InputChannel implements Serializable {
    private Long partitionId;
    private int subTaskIndex;

    public InputChannel(Long partitionId, int subTaskIndex) {
        this.partitionId = partitionId;
        this.subTaskIndex = subTaskIndex;
    }
}
