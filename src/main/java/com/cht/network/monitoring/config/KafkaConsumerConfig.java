package com.cht.network.monitoring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
//@EnableKafka
public class KafkaConsumerConfig {

    //@KafkaListener(topics = "my-topic")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
