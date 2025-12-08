package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.BatterySensor;
import com.smarthome.smart_home_iot.dto.kafka.BatteryKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.BatterySensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatteryService {

    private final ObjectMapper objectMapper;
    private final BatterySensorRepository batterySensorRepository;

    @KafkaListener(topics = "battery-topic", groupId = "sensor-group")
    public void batteryConsume(String message) {
        try {
            BatteryKafkaMessageDto batteryDto = objectMapper.readValue(message, BatteryKafkaMessageDto.class);

            BatterySensor batteryEntity = new BatterySensor();
            batteryEntity.setDeviceId(batteryDto.getDeviceId());
            batteryEntity.setBattery(batteryDto.getBattery());
            batteryEntity.setTimestamp(batteryDto.getTimestamp());

            batterySensorRepository.save(batteryEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
