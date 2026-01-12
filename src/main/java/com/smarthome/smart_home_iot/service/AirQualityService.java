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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AirQualityService {

    private final ObjectMapper objectMapper;
    private final AirQualityDocumentRepository airQualityDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "air-quality-topic", groupId = "sensor-group")
    public void airQualityConsume(String message) {
        try {
            // 1. Kafka 메시지 역직렬화
            AirQualityKafkaMessageDto airDto =
                    objectMapper.readValue(message, AirQualityKafkaMessageDto.class);

            // 2. MongoDB 저장 (원본 유지)
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

            // 3. Redis 저장용 데이터 가공 🔥
            Map<String, Object> redisValue = new HashMap<>();
            redisValue.put("deviceId", airDto.getDeviceId());
            redisValue.put("pm10", airDto.getPm10());
            redisValue.put("pm25", airDto.getPm25());
            redisValue.put("co2", airDto.getCo2());
            redisValue.put("voc", airDto.getVoc());
            redisValue.put("light", airDto.getLight());
            redisValue.put("gasLeak", airDto.isGasLeak());
            redisValue.put("smokeLevel", airDto.getSmokeLevel());
            redisValue.put(
                    "timestamp",
                    airDto.getTimestamp()
                            .atZone(KST)
                            .format(FORMATTER)
            );

            String key = "sensor:air_quality:" + airDto.getDeviceId();
            stringRedisTemplate.opsForValue()
                    .set(key, objectMapper.writeValueAsString(redisValue));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("AirQuality JSON Parsing Error", e);
        }
    }
}
