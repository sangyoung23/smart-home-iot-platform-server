package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.TemperatureSensor;
import com.smarthome.smart_home_iot.dto.kafka.TemperatureKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.TemperatureSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final ObjectMapper objectMapper;
    private final TemperatureSensorRepository temperatureSensorRepository;

    @KafkaListener(topics = "temperature-topic", groupId = "sensor-group")
    public void temperatureConsume(String message) {
        try {
            TemperatureKafkaMessageDto tempDto = objectMapper.readValue(message, TemperatureKafkaMessageDto.class);

            TemperatureSensor tempEntity = new TemperatureSensor();
            tempEntity.setDeviceId(tempDto.getDeviceId());
            tempEntity.setTemperature(tempDto.getTemperature());
            tempEntity.setTimestamp(tempDto.getTimestamp());

            temperatureSensorRepository.save(tempEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
