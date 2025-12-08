package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.PowerSensor;
import com.smarthome.smart_home_iot.dto.kafka.PowerKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.PowerSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PowerService {

    private final ObjectMapper objectMapper;
    private final PowerSensorRepository powerSensorRepository;

    @KafkaListener(topics = "power-topic", groupId = "sensor-group")
    public void powerConsume(String message) {
        try {
            PowerKafkaMessageDto powerDto =
                    objectMapper.readValue(message, PowerKafkaMessageDto.class);

            // 2) DTO → Entity 변환
            PowerSensor powerEntity = new PowerSensor();
            powerEntity.setDeviceId(powerDto.getDeviceId());
            powerEntity.setPowerUsage(powerDto.getPowerUsage());
            powerEntity.setVoltage(powerDto.getVoltage());
            powerEntity.setCurrent(powerDto.getCurrent());
            powerEntity.setEnergyTotal(powerDto.getEnergyTotal());
            powerEntity.setTimestamp(powerDto.getTimestamp());

            // 3) DB 저장
            powerSensorRepository.save(powerEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
