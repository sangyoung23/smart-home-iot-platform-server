package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_power_sensor")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PowerSensor {
    // 전력 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 id

    @Column(nullable = false)
    private String deviceId; // 디바이스 ID

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
    private LocalDateTime timestamp; // 데이터가 측정된 시간
}
