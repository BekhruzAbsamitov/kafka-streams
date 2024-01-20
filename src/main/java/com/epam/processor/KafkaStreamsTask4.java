package com.epam.processor;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaStreamsTask4 {

    private final Logger log  = LoggerFactory.getLogger(KafkaStreamsTask4.class.getSimpleName());

    @Bean
    public KStream<Object, Object> processStreamWithCustomSerdes(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream("task4").foreach((key, value) -> log.info("Before filtering -> Key - {} | Value - {}", key, value));

        KStream<Object, Object> filtered = streamsBuilder
                .stream("task4")
                .filter((key, value) -> Objects.nonNull(value));

        filtered.foreach((key, value) -> log.info("After filtering -> Key - {} | Value - {}", key, value));
        return filtered;
    }
}
