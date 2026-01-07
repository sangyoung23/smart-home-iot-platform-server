package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.document.AirQualityDocument;
import com.smarthome.smart_home_iot.dto.kafka.AirQualityKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.mongo.AirQualityDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirQualityService {

    private final ObjectMapper objectMapper;
    private final AirQualityDocumentRepository airQualityDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @KafkaListener(topics = "air-quality-topic", groupId = "sensor-group")
    public void airQualityConsume(String message) {
        try {

            // 1. Kafka 메세지 DTO 직렬화
            AirQualityKafkaMessageDto airDto = objectMapper.readValue(message, AirQualityKafkaMessageDto.class);

            // 2. MongoDB Document Raw 데이터 저장
            AirQualityDocument airQualityDoc = AirQualityDocument.builder()
                    .deviceId(airDto.getDeviceId())
                    .pm10(airDto.getPm10())
                    .pm25(airDto.getPm25())
                    .co2(airDto.getCo2())
                    .voc(airDto.getVoc())
                    .light(airDto.getLight())
                    .gasLeak(airDto.isGasLeak())
                    .smokeLevel(airDto.getSmokeLevel())
                    .timestamp(airDto.getTimestamp())
                    .build();

            airQualityDocumentRepository.save(airQualityDoc);

            // 3. Redis 최신 상태 저장
            String key = "sensor:air_quality:" + airDto.getDeviceId();
            String value = objectMapper.writeValueAsString(airDto);
            stringRedisTemplate.opsForValue().set(key, value);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("AirQuality JSON Parsing Error", e);
        }
    }
}
