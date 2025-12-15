package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_humidity_statistics",
        uniqueConstraints = {
                // device_id + stat_date + stat_hour 가 유일하도록 설정
                @UniqueConstraint(columnNames = {"device_id", "stat_date", "stat_hour"})
        }
)
@Getter
@NoArgsConstructor
public class HumidityStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(name = "device_id", nullable = false)
    private String deviceId; // 센서 디바이스 ID

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate; // 통계 기준 날짜

    @Column(name = "stat_hour", nullable = false)
    private int statHour; // 통계 기준 시간 (0~23)

    @Column(name = "avg_humidity", nullable = false)
    private double avgHumidity; // 해당 시간대 평균 습도

    @Column(name = "min_humidity", nullable = false)
    private double minHumidity; // 해당 시간대 최소 습도

    @Column(name = "max_humidity", nullable = false)
    private double maxHumidity; // 해당 시간대 최대 습도

    @Column(name = "sample_count", nullable = false)
    private int sampleCount; // 해당 시간대 측정된 데이터 개수

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 레코드 생성 시각
}
