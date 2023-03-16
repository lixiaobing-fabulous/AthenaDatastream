package com.athena.api.util;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class KeyGroupUtil {
    public static final int DEFAULT_LOWER_BOUND_MAX_PARALLELISM = 1 << 7;

    public static final int UPPER_BOUND_MAX_PARALLELISM = 1 << 15;

    public static int assignKeyToParallelOperator(Object key, int maxParallelism, int parallelism) {
        int keyGroup = MathUtil.murmurHash(key.hashCode()) % maxParallelism;
        return keyGroup * parallelism / maxParallelism;
    }

    public static int computeDefaultMaxParallelism(int operatorParallelism) {


        return Math.min(
                Math.max(
                        MathUtil.roundUpToPowerOfTwo(operatorParallelism + (operatorParallelism / 2)),
                        DEFAULT_LOWER_BOUND_MAX_PARALLELISM),
                UPPER_BOUND_MAX_PARALLELISM);
    }

}
