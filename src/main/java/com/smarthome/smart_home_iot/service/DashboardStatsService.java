package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.sensor.DailyStatisticsProjection;
import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardStatsResponse;
import com.smarthome.smart_home_iot.dto.dashboard.SensorStatsDto;
import com.smarthome.smart_home_iot.helper.StatisticsRepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardStatsService {

    private final StatisticsRepositoryHelper repositoryHelper;

    public DashboardStatsResponse getStats(
            SensorType sensorType,
            String deviceId,
            LocalDate from,
            LocalDate to) {

        // 1. 센서 통계 가져오기
        List<? extends DailyStatisticsProjection> stats =
                repositoryHelper.findDailyStats(sensorType, deviceId, from, to);

        // 2. DTO로 변환
        List<SensorStatsDto> sensorStats = stats.stream()
                .map(s -> SensorStatsDto.builder()
                        .stat_date(s.getDate())
                        .avg_value(s.getRepresentativeValue())
                        .build())
                .toList();

        // 3. DashboardStatsResponse 생성
        return DashboardStatsResponse.builder()
                .sensorType(sensorType)
                .deviceId(deviceId)
                .data(sensorStats)
                .build();
    }

}
