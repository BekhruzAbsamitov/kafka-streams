package com.epam.producer;

import com.epam.dto.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    private final Logger log = LoggerFactory.getLogger(KafkaMessagePublisher.class.getSimpleName());
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, Employee> employeeKafkaTemplate;

    @Autowired
    public KafkaMessagePublisher(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String, Employee> employeeKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.employeeKafkaTemplate = employeeKafkaTemplate;
    }

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send("task2", message);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            } else {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }

    public void sendEmployeeToTopic(Employee message) {
        CompletableFuture<SendResult<String, Employee>> completableFuture = employeeKafkaTemplate.send("task4", message);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            } else {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
