package com.athena.environment.transformation;

import com.athena.api.util.tupple.Tuple2;
import lombok.Data;

import java.util.Objects;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
@Data
public class Transformation<T> {
    private int parallelism;
    private int maxParallelism = 1 << 7;

    protected static Integer ID_COUNTER = 0;
    protected int id;
    protected String name;

    public static int getNewNodeId() {
        ID_COUNTER++;
        return ID_COUNTER;
    }

    public Transformation(String name, int parallelism) {
        this.id = getNewNodeId();
        this.name = name;
        this.parallelism = parallelism;
    }

    public Transformation() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transformation)) {
            return false;
        }
        return Objects.equals(((Transformation<?>) o).id, this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public int getMaxParallelism() {
        return maxParallelism;
    }
}
