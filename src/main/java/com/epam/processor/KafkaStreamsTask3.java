package com.epam.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
public class KafkaStreamsTask3 {

    private final Logger log  = LoggerFactory.getLogger(KafkaStreamsTask3.class.getSimpleName());

    @Bean
    public KStream<Long, String> processStream(StreamsBuilder streamsBuilder) {
        KStream<Long, String> stream1 = streamsBuilder.stream("task3-1")
                .filter((key, value) -> Objects.nonNull(value) && String.valueOf(value).contains(":"))
                .map((key, value) -> {
                    Long newKey = Long.valueOf(String.valueOf(value).split(":")[0]);
                    return new KeyValue<>(newKey, String.valueOf(value));
                });
        stream1.foreach((key, value) -> log.info("Stream1: Key - {} | Value - {}", key, value));

        KStream<Long, String> stream2 = streamsBuilder.stream("task3-2")
                .filter((key, value) -> Objects.nonNull(value) && String.valueOf(value).contains(":"))
                .map((key, value) -> {
                    Long newKey = Long.valueOf(String.valueOf(value).split(":")[0]);
                    return new KeyValue<>(newKey, String.valueOf(value));
                });
        stream2.foreach((key, value) -> log.info("Stream2: Key - {} | Value - {}", key, value));


        KStream<Long, String> joinedStream = stream1.join(stream2, (lestValue, rightValue) -> lestValue + "<>" + rightValue,
                JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(30), Duration.ofSeconds(30)));

        joinedStream.foreach((key, value) -> log.info("Joined stream: Key - {} | Value - {}", key, value));
        return joinedStream;
    }

}
