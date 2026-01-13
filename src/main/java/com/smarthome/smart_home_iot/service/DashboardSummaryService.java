package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.sensor.FullStatisticsProjection;
import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import com.smarthome.smart_home_iot.domain.sensor.DailyStatisticsProjection;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardSummaryResponse;
import com.smarthome.smart_home_iot.dto.dashboard.SensorStatusDto;
import com.smarthome.smart_home_iot.helper.StatisticsRepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {

    private final StatisticsRepositoryHelper repositoryHelper;

    public DashboardSummaryResponse getSummary() {
        // 1. 전체 센서 수
        long totalSensorCount = SensorType.values().length;

        // 2. 정상 작동 센서 수
        List<SensorStatusDto> sensors = Arrays.stream(SensorType.values())
                .map(this::checkSensorStatus)
                .collect(Collectors.toList());

        // 2.1 정상 센서 수만 카운트
        long normalCount = sensors.stream()
                .filter(SensorStatusDto::isNormal)
                .count();

        // 3. 최근 데이터 수신 시간
        LocalDateTime lastCollectedAt = repositoryHelper.findLatestCreatedAt();

        // 4. 금일 데이터 수
        long todayAggregationCount = repositoryHelper.countTodayAll();


        return DashboardSummaryResponse.builder()
                .totalSensorCount(totalSensorCount)
                .normalSensorCount(normalCount)
                .lastCollectedAt(lastCollectedAt)
                .todayAggregationCount(todayAggregationCount)
                .sensors(sensors)
                .build();
    }

    private SensorStatusDto checkSensorStatus(SensorType type) {
        Optional<? extends FullStatisticsProjection> statsOpt =
                repositoryHelper.findLatestBySensorType(type);

        if (statsOpt.isEmpty()) {
            return SensorStatusDto.builder()
                    .sensorType(type)
                    .normal(false)
                    .statusMessage("집계 데이터 없음")
                    .build();
        }

        FullStatisticsProjection stats = statsOpt.get();
        LocalDateTime lastCreatedAt = stats.getCreatedAt();

        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime todayStart =
                LocalDateTime.now(zone)
                        .toLocalDate()
                        .atStartOfDay();

        boolean isNormal = !lastCreatedAt.isBefore(todayStart);

        String message = isNormal ? "정상" : "금일 배치 미실행";

        return SensorStatusDto.builder()
                .sensorType(type)
                .normal(isNormal)
                .statusMessage(message)
                .lastDataTime(lastCreatedAt)
                    .currentValue(stats.getRepresentativeValue())
                .build();
    }
}
