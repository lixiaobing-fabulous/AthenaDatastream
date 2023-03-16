package com.athena.runtime.executiongraph;

import lombok.Data;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class JobInformation {
    private final Long jobId;

    /** Job name */
    private final String jobName;

    public JobInformation(Long jobId, String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
    }
}
