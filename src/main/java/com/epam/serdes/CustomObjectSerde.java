package com.epam.serdes;

import com.epam.dto.Employee;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class CustomObjectSerde implements Serde<Employee> {

    @Override
    public Serializer<Employee> serializer() {
        return new CustomSerializer();
    }

    @Override
    public Deserializer<Employee> deserializer() {
        return new CustomDeserializer();
    }
}
