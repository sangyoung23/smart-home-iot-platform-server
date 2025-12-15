package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.document.PowerDocument;
import com.smarthome.smart_home_iot.dto.kafka.PowerKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.mongo.PowerDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PowerService {

    private final ObjectMapper objectMapper;
    private final PowerDocumentRepository powerDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @KafkaListener(topics = "power-topic", groupId = "sensor-group")
    public void powerConsume(String message) {
        try {

            // 1. Kafka 메세지 DTO 직렬화
            PowerKafkaMessageDto powerDto = objectMapper.readValue(message, PowerKafkaMessageDto.class);

            // 2. MongoDB Document Raw 데이터 저장
            PowerDocument powerDoc = new PowerDocument();
            powerDoc.setDeviceId(powerDto.getDeviceId());
            powerDoc.setPowerUsage(powerDto.getPowerUsage());
            powerDoc.setVoltage(powerDto.getVoltage());
            powerDoc.setCurrent(powerDto.getCurrent());
            powerDoc.setEnergyTotal(powerDto.getEnergyTotal());
            powerDoc.setTimestamp(powerDto.getTimestamp());

            powerDocumentRepository.save(powerDoc);

            // 3. Redis 최신 상태 저장
            String key = "sensor:power:" + powerDto.getDeviceId();
            String value = objectMapper.writeValueAsString(powerDto);
            stringRedisTemplate.opsForValue().set(key, value);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Power JSON Parsing Error", e);
        }
    }
}
