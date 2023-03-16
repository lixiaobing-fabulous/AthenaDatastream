package com.athena.runtime.stream.task;

import lombok.SneakyThrows;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public interface Input {
    @SneakyThrows
    Object getElement();
}
