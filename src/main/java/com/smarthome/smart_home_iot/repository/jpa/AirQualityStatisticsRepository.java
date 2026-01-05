package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.sensor.AirQualityStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AirQualityStatisticsRepository extends JpaRepository<AirQualityStatistics, Long> {
    Optional<AirQualityStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );

    Optional<AirQualityStatistics> findTopByOrderByCreatedAtDesc();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
