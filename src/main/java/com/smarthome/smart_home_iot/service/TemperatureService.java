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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final ObjectMapper objectMapper;
    private final TemperatureDocumentRepository temperatureDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    // KST 기준
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "temperature-topic", groupId = "sensor-group")
    public void temperatureConsume(String message) {
        try {
            // 1. Kafka 메시지 역직렬화
            TemperatureKafkaMessageDto tempDto =
                    objectMapper.readValue(message, TemperatureKafkaMessageDto.class);

            // 2. MongoDB 저장 (원본 유지)
            TemperatureDocument tempDoc = TemperatureDocument.builder()
                    .deviceId(tempDto.getDeviceId())
                    .temperature(tempDto.getTemperature())
                    .timestamp(tempDto.getTimestamp())
                    .build();

            temperatureDocumentRepository.save(tempDoc);

            // 3. Redis 저장용 데이터 가공
            Map<String, Object> redisValue = new HashMap<>();
            redisValue.put("deviceId", tempDto.getDeviceId());
            redisValue.put("temperature", tempDto.getTemperature());
            redisValue.put(
                    "timestamp",
                    tempDto.getTimestamp()
                            .atZone(KST)
                            .format(FORMATTER)
            );

            String key = "sensor:temperature:" + tempDto.getDeviceId();
            stringRedisTemplate.opsForValue()
                    .set(key, objectMapper.writeValueAsString(redisValue));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Temperature JSON Parsing Error", e);
        }
    }
}
