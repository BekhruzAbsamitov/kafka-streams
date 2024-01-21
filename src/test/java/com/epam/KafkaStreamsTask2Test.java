package com.epam;

import com.epam.processor.KafkaStreamsTask2;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaStreamsTask2Test {

    @Test
    void shouldProcessStreamWithCustomSerdes() {
        String OUTPUT_TOPIC = "task2-2";
        String INPUT_TOPIC = "task2";
        final List<String> inputValues = Arrays.asList("I am a string with some words", "Another string", "Some words");

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KafkaStreamsTask2 kafkaStreamsTask2 = new KafkaStreamsTask2();
        Map<String, KStream<Integer, String>> stringKStreamMap = kafkaStreamsTask2.kStreamMap(streamsBuilder);
        KStream<Integer, String> integerStringKStream = kafkaStreamsTask2.wordsLongKStream(stringKStreamMap);
        KStream<Integer, String> integerStringKStream1 = kafkaStreamsTask2.wordsShortKStream(stringKStreamMap);
        kafkaStreamsTask2.printStreams(integerStringKStream, integerStringKStream1);


        Properties propertyConfig = getPropertyConfig();

        Topology topology = streamsBuilder.build();
        TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, propertyConfig);


        TestInputTopic<Integer, String> inputTopic =
                topologyTestDriver.createInputTopic(INPUT_TOPIC, new IntegerSerializer(), new StringSerializer());

        sendMessages(inputTopic, inputValues);


        TestOutputTopic<Integer, String> outputTopic = topologyTestDriver
                .createOutputTopic(OUTPUT_TOPIC, new IntegerDeserializer(), new StringDeserializer());

        List<String> results = outputTopic.readValuesToList();

        assertEquals(2, results.size());
        assertThat(results).contains("a", "am");

        topologyTestDriver.close();
    }

    private void sendMessages(final TestInputTopic<Integer, String> inputTopic, final List<String> messages) {
        messages.forEach(inputTopic::pipeInput);
    }

    private Properties getPropertyConfig() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return streamsConfiguration;
    }


}
