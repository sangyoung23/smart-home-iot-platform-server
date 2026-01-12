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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatteryService {

    private final ObjectMapper objectMapper;
    private final BatteryDocumentRepository batteryDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "battery-topic", groupId = "sensor-group")
    public void batteryConsume(String message) {
        try {
            // 1. Kafka 메시지 역직렬화
            BatteryKafkaMessageDto batteryDto =
                    objectMapper.readValue(message, BatteryKafkaMessageDto.class);

            // 2. MongoDB 저장 (원본 유지)
            BatteryDocument batteryDoc = BatteryDocument.builder()
                    .deviceId(batteryDto.getDeviceId())
                    .battery(batteryDto.getBattery())
                    .timestamp(batteryDto.getTimestamp())
                    .build();

            batteryDocumentRepository.save(batteryDoc);

            // 3. Redis 저장용 데이터 가공 🔥
            Map<String, Object> redisValue = new HashMap<>();
            redisValue.put("deviceId", batteryDto.getDeviceId());
            redisValue.put("battery", batteryDto.getBattery());
            redisValue.put(
                    "timestamp",
                    batteryDto.getTimestamp()
                            .atZone(KST)
                            .format(FORMATTER)
            );

            String key = "sensor:battery:" + batteryDto.getDeviceId();
            stringRedisTemplate.opsForValue()
                    .set(key, objectMapper.writeValueAsString(redisValue));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Battery JSON Parsing Error", e);
        }
    }
}
