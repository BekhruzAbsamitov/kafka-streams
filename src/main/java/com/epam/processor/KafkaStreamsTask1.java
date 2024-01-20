package com.epam.processor;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamsTask1 {

    private final StreamsBuilder streamsBuilder;

    @Autowired
    public KafkaStreamsTask1(StreamsBuilder streamsBuilder) {
        this.streamsBuilder = streamsBuilder;
    }

    @PostConstruct
    public void createStream() {
        streamsBuilder.stream("task1-1")
                .to("task1-2");
    }

}
