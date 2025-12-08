package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_battery_sensor")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BatterySensor {
    // 배터리 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 id

    @Column(nullable = false)
    private String deviceId; // 디바이스 ID

    @Column(nullable = false)
    @Min(0) @Max(100)
    private int battery; // 배터리 잔량(%)

    @Column(nullable = false)
    private LocalDateTime timestamp; // 데이터가 측정된 시간

}
