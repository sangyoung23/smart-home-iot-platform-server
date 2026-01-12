package com.smarthome.smart_home_iot.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BatteryKafkaMessageDto {
    // 배터리 DTO

    private String deviceId; // 디바이스 ID
    private int battery; // 배터리 잔량(%)
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
