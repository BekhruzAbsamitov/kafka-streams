package com.epam.controller;

import com.epam.dto.Employee;
import com.epam.producer.KafkaMessagePublisher;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka-streams")
public class KafkaController {

    private final KafkaMessagePublisher publisher;
    private final StreamsBuilderFactoryBean factoryBean;
    @Autowired
    public KafkaController(KafkaMessagePublisher publisher, StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.publisher = publisher;
        this.factoryBean = streamsBuilderFactoryBean;
    }

    @GetMapping("/count/{word}")
    public Long getWordCount(@PathVariable String word) {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore())
        );
        return counts.get(word);
    }

    @PostMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message) {
        publisher.sendMessageToTopic(message);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Message successfully published...");
    }

    @PostMapping("/send")
    public ResponseEntity<?> publishMessage(@RequestBody Employee message) {
        publisher.sendEmployeeToTopic(message);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Message successfully published...");
    }
}
