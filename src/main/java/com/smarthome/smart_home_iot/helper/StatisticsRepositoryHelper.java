package com.smarthome.smart_home_iot.helper;

import com.smarthome.smart_home_iot.domain.sensor.*;
import com.smarthome.smart_home_iot.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatisticsRepositoryHelper {

    // 5개의 Repository 주입
    private final PowerStatisticsRepository powerRepository;
    private final AirQualityStatisticsRepository airQualityRepository;
    private final TemperatureStatisticsRepository temperatureRepository;
    private final HumidityStatisticsRepository humidityRepository;
    private final BatteryStatisticsRepository batteryRepository;

    public List<? extends DailyStatisticsProjection> findDailyStats(
            SensorType type,
            String deviceId,
            LocalDate from,
            LocalDate to
    ) {
        return switch (type) {
            case TEMPERATURE ->
                    temperatureRepository.findByDeviceIdAndStatDateBetweenOrderByStatDate(deviceId, from, to);
            case HUMIDITY ->
                    humidityRepository.findByDeviceIdAndStatDateBetweenOrderByStatDate(deviceId, from, to);
            case AIR_QUALITY ->
                    airQualityRepository.findByDeviceIdAndStatDateBetweenOrderByStatDate(deviceId, from, to);
            case POWER ->
                    powerRepository.findByDeviceIdAndStatDateBetweenOrderByStatDate(deviceId, from, to);
            case BATTERY ->
                    batteryRepository.findByDeviceIdAndStatDateBetweenOrderByStatDate(deviceId, from, to);
        };
    }


    /**
     * 센서 타입에 따라 최근 통계 데이터 조회
     */
    public Optional<? extends FullStatisticsProjection> findLatestBySensorType(SensorType type) {
        return switch (type) {
            case POWER -> powerRepository.findTopByOrderByCreatedAtDesc();
            case AIR_QUALITY -> airQualityRepository.findTopByOrderByCreatedAtDesc();
            case TEMPERATURE -> temperatureRepository.findTopByOrderByCreatedAtDesc();
            case HUMIDITY -> humidityRepository.findTopByOrderByCreatedAtDesc();
            case BATTERY -> batteryRepository.findTopByOrderByCreatedAtDesc();
        };
    }

    /**
     * 전체 센서 중 가장 최근 집계 시간
     */
    public LocalDateTime findLatestCreatedAt() {
        return Arrays.stream(SensorType.values())
                .map(this::findLatestBySensorType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FullStatisticsProjection::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    /**
     * 금일 총 집계 건수
     */
    public long countTodayAll() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long powerCount = powerRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long airQualityCount = airQualityRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long temperatureCount = temperatureRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long humidityCount = humidityRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long batteryCount = batteryRepository.countByCreatedAtBetween(startOfDay, endOfDay);

        return powerCount + airQualityCount + temperatureCount + humidityCount + batteryCount;
    }
}