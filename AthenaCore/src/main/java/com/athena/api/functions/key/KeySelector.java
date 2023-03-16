package com.athena.api.functions.key;

import com.athena.api.functions.Function;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public interface KeySelector<IN, KEY> extends Function {

    KEY getKey(IN value) throws Exception;
}
