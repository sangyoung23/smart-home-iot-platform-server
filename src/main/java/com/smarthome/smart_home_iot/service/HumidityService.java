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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final ObjectMapper objectMapper;
    private final HumidityDocumentRepository humidityDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "humidity-topic", groupId = "sensor-group")
    public void humidityConsume(String message) {
        try {
            // 1. Kafka 메시지 역직렬화
            HumidityKafkaMessageDto humidityDto =
                    objectMapper.readValue(message, HumidityKafkaMessageDto.class);

            // 2. MongoDB 저장 (원본 유지)
            HumidityDocument humidityDoc = HumidityDocument.builder()
                    .deviceId(humidityDto.getDeviceId())
                    .humidity(humidityDto.getHumidity())
                    .timestamp(humidityDto.getTimestamp())
                    .build();

            humidityDocumentRepository.save(humidityDoc);

            // 3. Redis 저장용 데이터 가공 🔥
            Map<String, Object> redisValue = new HashMap<>();
            redisValue.put("deviceId", humidityDto.getDeviceId());
            redisValue.put("humidity", humidityDto.getHumidity());
            redisValue.put(
                    "timestamp",
                    humidityDto.getTimestamp()
                            .atZone(KST)
                            .format(FORMATTER)
            );

            String key = "sensor:humidity:" + humidityDto.getDeviceId();
            stringRedisTemplate.opsForValue()
                    .set(key, objectMapper.writeValueAsString(redisValue));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Humidity JSON Parsing Error", e);
        }
    }
}
