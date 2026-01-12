package com.smarthome.smart_home_iot.dto.kafka;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class TemperatureKafkaMessageDto {
    // 온도 DTO

    private String deviceId; // 디바이스 ID
    private Double temperature; // 온도
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
