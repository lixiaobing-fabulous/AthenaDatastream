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
public class InputGate implements Serializable {
    private InputChannel[] inputChannels;

    public InputGate(InputChannel[] inputChannels) {
        this.inputChannels = inputChannels;
    }
}
