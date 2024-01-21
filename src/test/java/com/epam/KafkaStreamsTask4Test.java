package com.epam;

import com.epam.dto.Employee;
import com.epam.processor.KafkaStreamsTask4;
import com.epam.serdes.CustomDeserializer;
import com.epam.serdes.CustomObjectSerde;
import com.epam.serdes.CustomSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaStreamsTask4Test {

    @Test
    void shouldProcessStreamWithCustomSerdes() {
        String OUTPUT_TOPIC = "task4-2";
        String INPUT_TOPIC = "task4";
        final Map<String, Employee> inputValues = new HashMap<>();
        inputValues.put("k1", new Employee("name", "company", "position", 1));

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KafkaStreamsTask4 kafkaStreamsTask4 = new KafkaStreamsTask4();
        kafkaStreamsTask4.processStreamWithCustomSerdes(streamsBuilder);

        Properties propertyConfig = getPropertyConfig();

        Topology topology = streamsBuilder.build();
        TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, propertyConfig);


        TestInputTopic<String, Employee> inputTopic =
                topologyTestDriver.createInputTopic(INPUT_TOPIC, new StringSerializer(), new CustomSerializer());

        sendMessages(inputTopic, inputValues);


        TestOutputTopic<String, Employee> outputTopic = topologyTestDriver
                .createOutputTopic(OUTPUT_TOPIC, new StringDeserializer(), new CustomDeserializer());

        List<Employee> readValuesToList = outputTopic.readValuesToList();

        assertEquals(1, readValuesToList.size());
        assertThat(readValuesToList.get(0).getName()).isEqualTo("name");

        topologyTestDriver.close();
    }

    private void sendMessages(final TestInputTopic<String, Employee> inputTopic, final Map<String, Employee> messages) {
        messages.forEach(inputTopic::pipeInput);
    }

    private Properties getPropertyConfig() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CustomObjectSerde.class.getName());
        return streamsConfiguration;
    }


}
