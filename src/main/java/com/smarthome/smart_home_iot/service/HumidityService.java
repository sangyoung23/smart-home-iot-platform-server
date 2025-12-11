package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.document.HumidityDocument;
import com.smarthome.smart_home_iot.dto.kafka.HumidityKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.mongo.HumidityDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final ObjectMapper objectMapper;
    private final HumidityDocumentRepository humidityDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @KafkaListener(topics = "humidity-topic", groupId = "sensor-group")
    public void humidityConsume(String message) {
        try {

            // 1. Kafka 메세지 DTO 직렬화
            HumidityKafkaMessageDto humidityDto = objectMapper.readValue(message, HumidityKafkaMessageDto.class);

            // 2. MongoDB Document Raw 데이터 저장
            HumidityDocument humidityDoc = new HumidityDocument();
            humidityDoc.setDeviceId(humidityDto.getDeviceId());
            humidityDoc.setHumidity(humidityDto.getHumidity());
            humidityDoc.setTimestamp(humidityDto.getTimestamp());

            humidityDocumentRepository.save(humidityDoc);

            // 3. Redis 최신 상태 저장
//            String key = "sensor:humidity:" + humidityDto.getDeviceId();
//            String value = objectMapper.writeValueAsString(humidityDto);
//            stringRedisTemplate.opsForValue().set(key, value);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Humidity JSON Parsing Error", e);
        }
    }
}
