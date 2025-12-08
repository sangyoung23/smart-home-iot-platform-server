package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_sensor_info")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId; // 디바이스 ID

    @Column(nullable = false)
    @DecimalMin(value = "-20.0") @DecimalMax(value = "60.0")
    private Double temperature; // 온도

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double humidity; // 습도

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
    @DecimalMin("0.0") @DecimalMax("5000.0")
    private Double powerUsage; // 전력 사용량(W)

    @Column(nullable = false)
    @DecimalMin("180.0") @DecimalMax("250.0")
    private Double voltage; // 전압(V)

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("30.0")
    private Double current; // 전류(A)

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("1000000.0")
    private Double energyTotal; // 누적 전력(kWh)

    @Column(nullable = false)
    private boolean gasLeak; // 가스 감지 여부

    @Column(nullable = false)
    @Min(0) @Max(1000)
    private int smokeLevel; // 연기 농도

    @Column(nullable = false)
    @Min(0) @Max(100)
    private int battery; // 배터리 잔량(%)

    @Column(nullable = false)
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
