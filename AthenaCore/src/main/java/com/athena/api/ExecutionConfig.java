package com.athena.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class ExecutionConfig implements Serializable {
    private int parallelism = 1;
}
