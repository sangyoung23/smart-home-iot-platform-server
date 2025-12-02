package com.smarthome.smart_home_iot.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_sensor_info")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId; // 디바이스 고유 ID

    @Column(nullable = false)
    private double temperature; // 센서가 측정한 온도 값

    @Column(nullable = false)
    private double humidity; // 센서가 측정한 습도 값

    @Column(nullable = false)
    private long timestamp; // 데이터가 측정된 시간
}
