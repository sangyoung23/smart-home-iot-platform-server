package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import com.smarthome.smart_home_iot.dto.dashboard.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardRealtimeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public DashboardRealtimeResponse getRealtime(String deviceId) {

        List<RealtimeSensorStatus> sensors = new ArrayList<>();
        String latestTime = null;

        for (SensorType sensorType : SensorType.values()) {

            String key = "sensor:" + sensorType.name().toLowerCase() + ":" + deviceId;
            String value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                sensors.add(
                        RealtimeSensorStatus.builder()
                                .sensorType(sensorType)
                                .statusMessage("실시간 데이터 없음")
                                .normal(false)
                                .build()
                );
                continue;
            }

            try {
                switch (sensorType) {

                    case TEMPERATURE -> {
                        TemperatureRedisPayload payload =
                                objectMapper.readValue(value, TemperatureRedisPayload.class);

                        latestTime = payload.getTimestamp();

                        sensors.add(
                                RealtimeSensorStatus.builder()
                                        .sensorType(sensorType)
                                        .currentValue(payload.getTemperature())
                                        .lastDataTime(payload.getTimestamp())
                                        .normal(true)
                                        .statusMessage("정상 수신")
                                        .build()
                        );
                    }

                    case HUMIDITY -> {
                        HumidityRedisPayload payload =
                                objectMapper.readValue(value, HumidityRedisPayload.class);

                        latestTime = payload.getTimestamp();

                        sensors.add(
                                RealtimeSensorStatus.builder()
                                        .sensorType(sensorType)
                                        .currentValue(payload.getHumidity())
                                        .lastDataTime(payload.getTimestamp())
                                        .normal(true)
                                        .statusMessage("정상 수신")
                                        .build()
                        );
                    }

                    case AIR_QUALITY -> {
                        AirQualityRedisPayload payload =
                                objectMapper.readValue(value, AirQualityRedisPayload.class);

                        latestTime = payload.getTimestamp();

                        sensors.add(
                                RealtimeSensorStatus.builder()
                                        .sensorType(sensorType)
                                        .currentValue(payload.getPm25())
                                        .lastDataTime(payload.getTimestamp())
                                        .normal(true)
                                        .statusMessage("정상 수신")
                                        .build()
                        );
                    }

                    case POWER -> {
                        PowerRedisPayload payload =
                                objectMapper.readValue(value, PowerRedisPayload.class);

                        latestTime = payload.getTimestamp();

                        sensors.add(
                                RealtimeSensorStatus.builder()
                                        .sensorType(sensorType)
                                        .currentValue(payload.getPowerUsage())
                                        .lastDataTime(payload.getTimestamp())
                                        .normal(true)
                                        .statusMessage("정상 수신")
                                        .build()
                        );
                    }

                    case BATTERY -> {
                        BatteryRedisPayload payload =
                                objectMapper.readValue(value, BatteryRedisPayload.class);

                        latestTime = payload.getTimestamp();

                        sensors.add(
                                RealtimeSensorStatus.builder()
                                        .sensorType(sensorType)
                                        .currentValue(payload.getBattery())
                                        .lastDataTime(payload.getTimestamp())
                                        .normal(true)
                                        .statusMessage("정상 수신")
                                        .build()
                        );
                    }
                }

            } catch (Exception e) {
                sensors.add(
                        RealtimeSensorStatus.builder()
                                .sensorType(sensorType)
                                .statusMessage("데이터 파싱 오류")
                                .normal(false)
                                .build()
                );
            }
        }

        return DashboardRealtimeResponse.builder()
                .deviceId(deviceId)
                .lastUpdatedAt(latestTime)
                .sensors(sensors)
                .build();
    }
}
