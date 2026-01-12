package com.smarthome.smart_home_iot.dto.kafka;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AirQualityKafkaMessageDto {
    // 공기질 DTO

    private String deviceId; // 디바이스 ID
    private Double pm10; // 미세먼지 PM10
    private Double pm25; // 미세먼지 PM2.5
    private int co2; // CO2 농도
    private int voc; // VOC 수치
    private int light; // 조도
    private boolean gasLeak; // 가스 감지 여부
    private int smokeLevel; // 연기 농도
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
