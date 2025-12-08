package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.AirQualitySensor;
import com.smarthome.smart_home_iot.dto.kafka.AirQualityKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.AirQualitySensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirQualityService {

    private final ObjectMapper objectMapper;
    private final AirQualitySensorRepository airQualitySensorRepository;

    @KafkaListener(topics = "air-quality-topic", groupId = "sensor-group")
    public void airQualityConsume(String message) {
        try {
            AirQualityKafkaMessageDto airDto = objectMapper.readValue(message, AirQualityKafkaMessageDto.class);

            AirQualitySensor airEntity = new AirQualitySensor();
            airEntity.setDeviceId(airDto.getDeviceId());
            airEntity.setPm10(airDto.getPm10());
            airEntity.setPm25(airDto.getPm25());
            airEntity.setCo2(airDto.getCo2());
            airEntity.setVoc(airDto.getVoc());
            airEntity.setLight(airDto.getLight());
            airEntity.setGasLeak(airDto.isGasLeak());
            airEntity.setSmokeLevel(airDto.getSmokeLevel());

            airQualitySensorRepository.save(airEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
