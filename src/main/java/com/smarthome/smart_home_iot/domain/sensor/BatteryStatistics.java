package com.smarthome.smart_home_iot.domain.sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_battery_statistics",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"device_id", "stat_date", "stat_hour"})
        }
)
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatteryStatistics implements FullStatisticsProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(name = "device_id", nullable = false)
    private String deviceId; // 센서 디바이스 ID

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate; // 통계 기준 날짜

    @Column(name = "stat_hour", nullable = false)
    private int statHour; // 통계 기준 시간 (0~23)

    @Column(name = "avg_battery", nullable = false)
    private double avgBattery; // 평균 배터리 잔량

    @Column(name = "min_battery", nullable = false)
    private int minBattery; // 최소 배터리 잔량

    @Column(name = "max_battery", nullable = false)
    private int maxBattery; // 최대 배터리 잔량

    @Column(name = "sample_count", nullable = false)
    private int sampleCount; // 통계 계산에 사용된 데이터 개수

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 레코드 생성 시각

    @Override
    public LocalDate getDate() {
        return this.statDate;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Double getRepresentativeValue() {
        return this.avgBattery;
    }

    public void merge(BatteryStatistics incoming) {
        int totalCount = this.sampleCount + incoming.sampleCount;

        this.avgBattery =
                ((this.avgBattery * this.sampleCount)
                        + (incoming.avgBattery * incoming.sampleCount))
                        / totalCount;

        this.minBattery = Math.min(this.minBattery, incoming.minBattery);

        this.maxBattery = Math.max(this.maxBattery, incoming.maxBattery);

        this.sampleCount = totalCount;
    }

}
