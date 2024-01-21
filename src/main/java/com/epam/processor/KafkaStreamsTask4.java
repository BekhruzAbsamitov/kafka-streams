package com.epam.processor;

import com.epam.dto.Employee;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaStreamsTask4 {

    private final Logger log = LoggerFactory.getLogger(KafkaStreamsTask4.class.getSimpleName());

    @Bean
    public KStream<String, Employee> processStreamWithCustomSerdes(StreamsBuilder streamsBuilder) {
        logProcessing(streamsBuilder.stream("task4"), "Before filtering");

        KStream<String, Employee> filtered = streamsBuilder
                .stream("task4")
                .filter((key, value) -> Objects.nonNull(value))
                .map((key, value) -> new KeyValue<>(String.valueOf(key), (Employee) value));

        filtered.to("task4-2");

        logProcessing(filtered, "After filtering");
        return filtered;
    }

    public void logProcessing(KStream<String, Employee> stream, String logPrefix) {
        stream.foreach((key, value) -> log.info("{} -> Key - {} | Value - {}", logPrefix, key, value));
    }
}
