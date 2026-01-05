package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import com.smarthome.smart_home_iot.domain.sensor.StatisticsEntity;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardSummaryResponse;
import com.smarthome.smart_home_iot.dto.dashboard.SensorStatusDto;
import com.smarthome.smart_home_iot.helper.StatisticsRepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {

    private final StatisticsRepositoryHelper repositoryHelper;

    private static final int AGGREGATION_DELAY_TOLERANCE_MINUTES = 10;

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
        Optional<? extends StatisticsEntity> statsOpt =
                repositoryHelper.findLatestBySensorType(type);

        // 데이터 없음
        if (statsOpt.isEmpty()) {
            return SensorStatusDto.builder()
                    .sensorType(type)
                    .normal(false)
                    .statusMessage("집계 데이터 없음")
                    .build();
        }

        StatisticsEntity stats = statsOpt.get();
        LocalDateTime lastCreatedAt = stats.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        // 예상 집계 시간 (매시 정각)
        LocalDateTime expectedAggregation = now
                .withMinute(0).withSecond(0).withNano(0);
        LocalDateTime allowedTime = expectedAggregation
                .plusMinutes(AGGREGATION_DELAY_TOLERANCE_MINUTES);

        // 정상 여부 판단: 예상 시간 이후 집계되었거나 아직 여유 시간 내
        boolean isNormal = !lastCreatedAt.isBefore(expectedAggregation)
                || now.isBefore(allowedTime);

        String message = isNormal ? "정상" : "집계 지연";

        return SensorStatusDto.builder()
                .sensorType(type)
                .normal(isNormal)
                .statusMessage(message)
                .lastDataTime(lastCreatedAt)
                .currentValue(stats.getRepresentativeValue())
                .build();
    }
}
