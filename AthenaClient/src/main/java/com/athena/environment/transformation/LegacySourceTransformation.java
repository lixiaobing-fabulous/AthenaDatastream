package com.athena.environment.transformation;

import com.athena.runtime.stream.api.operators.StreamOperatorFactory;
import lombok.Data;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class LegacySourceTransformation<T> extends Transformation<T> {

    private StreamOperatorFactory<T> operatorFactory;
    public LegacySourceTransformation(
            String name,
            StreamOperatorFactory<T> operatorFactory,
            int parallelism) {
        super(name, parallelism);
        this.operatorFactory = operatorFactory;
    }

}
