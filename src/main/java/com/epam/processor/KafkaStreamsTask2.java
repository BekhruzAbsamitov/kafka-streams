package com.epam.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class KafkaStreamsTask2 {

    private final Logger log  = LoggerFactory.getLogger(KafkaStreamsTask2.class.getSimpleName());

    @Bean
    public Map<String, KStream<Integer, String>> kStreamMap(StreamsBuilder streamsBuilder) {
        KStream<Object, Object> sourceStream = streamsBuilder
                .stream("task2")
                .filter((key, value) -> Objects.nonNull(value));

        KStream<Integer, String> processedStream = sourceStream
                .flatMapValues(value -> Arrays.asList(String.valueOf(value).split(" ")))
                .map(((key, value) -> new KeyValue<>(value.length(), value)));

        processedStream.foreach(((key, value) -> log.info("Key: {} | Value: {}", key, value)));

        Map<String, KStream<Integer, String>> wordsStreamsMap = new HashMap<>();
        wordsStreamsMap.put("words-short", processedStream.filter(((key, value) -> key < 10)));
        wordsStreamsMap.put("words-long", processedStream.filter(((key, value) -> key >= 10)));

        return wordsStreamsMap;
    }

    @Bean
    public KStream<Integer, String> wordsShortKStream(@Qualifier("kStreamMap") Map<String, KStream<Integer, String>> kStreamMap) {
        return kStreamMap.get("words-short").filter(((key, value) -> value.contains("a")));
    }

    @Bean
    public KStream<Integer, String> wordsLongKStream(@Qualifier("kStreamMap") Map<String, KStream<Integer, String>> kStreamMap) {
        return kStreamMap.get("words-long").filter(((key, value) -> value.contains("a")));
    }

    @Bean
    public KStream<Integer, String> printStreams(KStream<Integer, String> wordsShortKStream,
                                                 KStream<Integer, String> wordsLongKStream) {
        KStream<Integer, String> mergedStreams = wordsShortKStream.merge(wordsLongKStream);
        mergedStreams.foreach(((key, value) -> log.info("Print - Key: {} | Value: {}", key, value)));
        return mergedStreams;
    }

}
