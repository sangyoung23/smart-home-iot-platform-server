package com.smarthome.smart_home_iot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // 메시지 보내는 메서드
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Send Message: " + message + " to topic: " + topic);
    }
}
