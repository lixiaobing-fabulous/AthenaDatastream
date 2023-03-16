package com.athena.runtime.stream.streamrecord;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class StreamRecord<T> {
    private T value;

    public StreamRecord(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public <X> StreamRecord<X> replace(X element) {
        this.value = (T) element;
        return (StreamRecord<X>) this;
    }

}
