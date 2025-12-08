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
@Table(name = "tb_humidity_sensor")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HumiditySensor {
    // 습도 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 id

    @Column(nullable = false)
    private String deviceId; // 디바이스 ID

    @Column(nullable = false)
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double humidity; // 습도

    @Column(nullable = false)
    private LocalDateTime timestamp; // 데이터가 측정된 시간

}
