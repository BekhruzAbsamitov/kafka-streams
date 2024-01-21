package com.epam.serdes;

import com.epam.dto.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomDeserializer implements Deserializer<Employee> {

    private final Logger log = LoggerFactory.getLogger(CustomDeserializer.class.getSimpleName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Deserialize a record value from a byte array into a value or object.
     *
     * @param topic topic associated with the data
     * @param data  serialized bytes; may be null; implementations are recommended to handle null by returning a value or null rather than throwing an exception.
     * @return deserialized typed data; may be null
     */
    @Override
    public Employee deserialize(String topic, byte[] data) {
        log.info("Deserializing...");
        try {
            return objectMapper.readValue(data, Employee.class);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when deserializing bytes object to Employee object");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
