package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_air_quality_sensor")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AirQualitySensor {
    // 공기질 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 id

    @Column(nullable = false)
    private String deviceId; // 디바이스 ID

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("600.0")
    private Double pm10; // 미세먼지 PM10

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("500.0")
    private Double pm25; // 미세먼지 PM2.5

    @Column(nullable = false)
    @Min(0) @Max(5000)
    private int co2; // CO2 농도

    @Column(nullable = false)
    @Min(0) @Max(1000)
    private int voc; // VOC 수치

    @Column(nullable = false)
    @Min(0) @Max(100000)
    private int light; // 조도

    @Column(nullable = false)
    private boolean gasLeak; // 가스 감지 여부

    @Column(nullable = false)
    @Min(0) @Max(1000)
    private int smokeLevel; // 연기 농도

    @Column(nullable = false)
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
