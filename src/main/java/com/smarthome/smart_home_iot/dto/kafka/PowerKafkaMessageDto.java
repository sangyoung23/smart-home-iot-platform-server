package com.smarthome.smart_home_iot.dto.kafka;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PowerKafkaMessageDto {
    // 전력 DTO

    private String deviceId; // 디바이스 ID
    private Double powerUsage; // 전력 사용량(W)
    private Double voltage; // 전압(V)
    private Double current; // 전류(A)
    private Double energyTotal; // 누적 전력(kWh)
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
