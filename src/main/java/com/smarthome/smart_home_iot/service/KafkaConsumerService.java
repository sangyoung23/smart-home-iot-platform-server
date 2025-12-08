package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.Sensor;
import com.smarthome.smart_home_iot.repository.KafkaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final KafkaRepository kafkaRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sensor-topic", groupId = "sensor-group")
    public void consume(String message) {
        try {
            Sensor sensor = objectMapper.readValue(message, Sensor.class);
             kafkaRepository.save(sensor);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Sensor parseMessage(String message) {
        try {
            return objectMapper.readValue(message, Sensor.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
