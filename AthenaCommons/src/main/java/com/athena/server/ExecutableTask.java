package com.athena.server;

import lombok.Data;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
@Data
public class ExecutableTask implements Serializable {
    private byte[] executable;
    private URL[] jars;

    public ExecutableTask(byte[] executable, URL[] jars) {
        this.executable = executable;
        this.jars = jars;
    }

    public ExecutableTask() {
    }
}
