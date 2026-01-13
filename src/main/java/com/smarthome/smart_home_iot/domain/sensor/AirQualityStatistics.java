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
        name = "tb_air_quality_statistics",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"device_id", "stat_date", "stat_hour"})
        }
)
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityStatistics implements FullStatisticsProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(name = "device_id", nullable = false)
    private String deviceId; // 센서 디바이스 ID

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate; // 통계 기준 날짜

    @Column(name = "stat_hour", nullable = false)
    private int statHour; // 통계 기준 시간 (0~23)

    @Column(name = "avg_pm10", nullable = false)
    private double avgPm10; // 평균 미세먼지(PM10)

    @Column(name = "avg_pm25", nullable = false)
    private double avgPm25; // 평균 초미세먼지(PM2.5)

    @Column(name = "avg_co2", nullable = false)
    private double avgCo2; // 평균 CO2

    @Column(name = "avg_voc", nullable = false)
    private double avgVoc; // 평균 VOC

    @Column(name = "avg_light", nullable = false)
    private double avgLight; // 평균 조도

    @Column(name = "gas_leak_count", nullable = false)
    private int gasLeakCount; // 가스 누출 발생 횟수

    @Column(name = "avg_smoke_level", nullable = false)
    private double avgSmokeLevel; // 평균 연기 수준

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
        return this.avgPm25;
    }

    public void merge(AirQualityStatistics other) {
        int totalCount = this.sampleCount + other.sampleCount;

        this.avgPm10 =
                ((this.avgPm10 * this.sampleCount)
                        + (other.avgPm10 * other.sampleCount))
                        / totalCount;

        this.avgPm25 =
                ((this.avgPm25 * this.sampleCount)
                        + (other.avgPm25 * other.sampleCount))
                        / totalCount;

        this.avgCo2 =
                ((this.avgCo2 * this.sampleCount)
                        + (other.avgCo2 * other.sampleCount))
                        / totalCount;

        this.avgVoc =
                ((this.avgVoc * this.sampleCount)
                        + (other.avgVoc * other.sampleCount))
                        / totalCount;

        this.avgLight =
                ((this.avgLight * this.sampleCount)
                        + (other.avgLight * other.sampleCount))
                        / totalCount;

        this.avgSmokeLevel =
                ((this.avgSmokeLevel * this.sampleCount)
                        + (other.avgSmokeLevel * other.sampleCount))
                        / totalCount;

        this.gasLeakCount += other.gasLeakCount;
        this.sampleCount = totalCount;
    }

}
