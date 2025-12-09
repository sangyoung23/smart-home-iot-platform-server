package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.document.TemperatureDocument;
import com.smarthome.smart_home_iot.dto.kafka.TemperatureKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.mongo.TemperatureDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final ObjectMapper objectMapper;
    private final TemperatureDocumentRepository temperatureDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @KafkaListener(topics = "temperature-topic", groupId = "sensor-group")
    public void temperatureConsume(String message) {
        try {

            // 1. Kafka 메세지 DTO 직렬화
            TemperatureKafkaMessageDto tempDto = objectMapper.readValue(message, TemperatureKafkaMessageDto.class);

            // 2. MongoDB Document Raw 데이터 저장
            TemperatureDocument tempDoc = new TemperatureDocument();
            tempDoc.setDeviceId(tempDto.getDeviceId());
            tempDoc.setTemperature(tempDto.getTemperature());
            tempDoc.setTimestamp(tempDto.getTimestamp());

            temperatureDocumentRepository.save(tempDoc);

            // 3. Redis 최신 상태 저장
            String key = "sensor:temperature:" + tempDto.getDeviceId();
            String value = objectMapper.writeValueAsString(tempDto);
            stringRedisTemplate.opsForValue().set(key, value);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Temperature JSON Parsing Error", e);
        }
    }
}
