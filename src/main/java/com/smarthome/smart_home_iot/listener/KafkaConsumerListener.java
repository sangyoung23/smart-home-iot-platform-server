package com.smarthome.smart_home_iot.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @KafkaListener
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
