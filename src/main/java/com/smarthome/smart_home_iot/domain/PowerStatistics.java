package com.smarthome.smart_home_iot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_power_statistics",
        uniqueConstraints = {
                // device_id + stat_date + stat_hour 가 유일하도록 설정
                @UniqueConstraint(columnNames = {"device_id", "stat_date", "stat_hour"})
        }
)
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PowerStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(name = "device_id", nullable = false)
    private String deviceId; // 센서 디바이스 ID

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate; // 통계 기준 날짜

    @Column(name = "stat_hour", nullable = false)
    private int statHour; // 통계 기준 시간 (0~23)

    @Column(name = "avg_power_usage", nullable = false)
    private double avgPowerUsage; // 평균 전력 사용량

    @Column(name = "min_power_usage", nullable = false)
    private double minPowerUsage; // 최소 전력 사용량

    @Column(name = "max_power_usage", nullable = false)
    private double maxPowerUsage; // 최대 전력 사용량

    @Column(name = "avg_voltage", nullable = false)
    private double avgVoltage; // 평균 전압

    @Column(name = "avg_current", nullable = false)
    private double avgCurrent; // 평균 전류

    @Column(name = "total_energy", nullable = false)
    private double totalEnergy; // 합계 에너지 (kWh 등)

    @Column(name = "sample_count", nullable = false)
    private int sampleCount; // 통계에 사용된 데이터 개수

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 레코드 생성 시각
}
