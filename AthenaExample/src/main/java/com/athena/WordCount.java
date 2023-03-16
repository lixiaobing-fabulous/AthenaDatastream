package com.athena;


import com.athena.api.functions.Collector;
import com.athena.api.functions.FlatMapFunction;
import com.athena.api.functions.Function;
import com.athena.api.util.tupple.Tuple2;
import com.athena.environment.StreamingEnvironment;
import com.athena.runtime.stream.api.operators.AbstractUdfStreamOperator;
import com.athena.runtime.stream.api.operators.OneInputStreamOperator;
import com.athena.runtime.stream.streamrecord.StreamRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/11
 * @Version 1.0
 **/
public class WordCount {
    public static void main(String[] args) {
        StreamingEnvironment env = StreamingEnvironment.getRuntimeEnvironment().setLocal();
        env.setParallelism(1);
        env.fromElements("hello world", "hello world")
                .flatMap(new Tokenizer())
                .setParallelism(1)
                .filter(r -> {
                    System.out.println("filter " + r.f0);
                    return r.f0.equals("hello");
                })
                .setParallelism(2)
                .keyBy(r -> r.f0)
                .transform("Keyed Reduce", new StreamGroupedReduce())
                .setParallelism(2)
                .print();
        env.execute("Streaming WordCount");
    }


    public static class StreamGroupedReduce extends AbstractUdfStreamOperator<Tuple2<String, Integer>, Function>
            implements OneInputStreamOperator<Tuple2<String, Integer>, Tuple2<String, Integer>> {
        public StreamGroupedReduce() {
            super(null);
        }

        private Map<String, Integer> cache = new ConcurrentHashMap<>();

        @Override
        public void processElement(StreamRecord<Tuple2<String, Integer>> element) throws Exception {
            Tuple2<String, Integer> value = element.getValue();
            Integer count = cache.getOrDefault(value.f0, 0);
            count++;
            cache.put(value.f0, count);
            System.out.println("Group by" + new Tuple2<>(value.f0, count));
            value.f1 = count;
            output.collect(new StreamRecord<>(new Tuple2<>(value.f0, count)));
        }
    }


    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            String[] tokens = value.toLowerCase().split("\\W+");

            for (String token : tokens) {
                if (token.length() > 0) {
                    System.out.println("FLAT MAP" + new Tuple2<>(token, 1));
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }

}
