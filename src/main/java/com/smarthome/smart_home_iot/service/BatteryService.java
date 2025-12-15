package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.document.BatteryDocument;
import com.smarthome.smart_home_iot.dto.kafka.BatteryKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.mongo.BatteryDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatteryService {

    private final ObjectMapper objectMapper;
    private final BatteryDocumentRepository batteryDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;


    @KafkaListener(topics = "battery-topic", groupId = "sensor-group")
    public void batteryConsume(String message) {
        try {

            // 1. Kafka 메세지 DTO 직렬화
            BatteryKafkaMessageDto batteryDto = objectMapper.readValue(message, BatteryKafkaMessageDto.class);

            // 2. MongoDB Document Raw 데이터 저장
            BatteryDocument batteryDoc = BatteryDocument.builder()
                    .deviceId(batteryDto.getDeviceId())
                    .battery(batteryDto.getBattery())
                    .timestamp(batteryDto.getTimestamp())
                    .build();

            batteryDocumentRepository.save(batteryDoc);

            // 3. Redis 최신 상태 저장
            String key = "sensor:battery:" + batteryDto.getDeviceId();
            String value = objectMapper.writeValueAsString(batteryDto);
            stringRedisTemplate.opsForValue().set(key, value);


        } catch (JsonProcessingException e) {
            throw new RuntimeException("Battery JSON Parsing Error", e);
        }
    }
}
