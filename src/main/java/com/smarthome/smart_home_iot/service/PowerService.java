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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PowerService {

    private final ObjectMapper objectMapper;
    private final PowerDocumentRepository powerDocumentRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "power-topic", groupId = "sensor-group")
    public void powerConsume(String message) {
        try {
            // 1. Kafka 메시지 역직렬화
            PowerKafkaMessageDto powerDto =
                    objectMapper.readValue(message, PowerKafkaMessageDto.class);

            // 2. MongoDB 저장 (원본 유지)
            PowerDocument powerDoc = PowerDocument.builder()
                    .deviceId(powerDto.getDeviceId())
                    .powerUsage(powerDto.getPowerUsage())
                    .voltage(powerDto.getVoltage())
                    .current(powerDto.getCurrent())
                    .energyTotal(powerDto.getEnergyTotal())
                    .timestamp(powerDto.getTimestamp())
                    .build();

            powerDocumentRepository.save(powerDoc);

            // 3. Redis 저장용 데이터 가공 🔥
            Map<String, Object> redisValue = new HashMap<>();
            redisValue.put("deviceId", powerDto.getDeviceId());
            redisValue.put("powerUsage", powerDto.getPowerUsage());
            redisValue.put("voltage", powerDto.getVoltage());
            redisValue.put("current", powerDto.getCurrent());
            redisValue.put("energyTotal", powerDto.getEnergyTotal());
            redisValue.put(
                    "timestamp",
                    powerDto.getTimestamp()
                            .atZone(KST)
                            .format(FORMATTER)
            );

            String key = "sensor:power:" + powerDto.getDeviceId();
            stringRedisTemplate.opsForValue()
                    .set(key, objectMapper.writeValueAsString(redisValue));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Power JSON Parsing Error", e);
        }
    }
}
