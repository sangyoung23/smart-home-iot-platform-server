package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.sensor.DailyStatisticsProjection;
import com.smarthome.smart_home_iot.domain.sensor.TemperatureStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TemperatureStatisticsRepository extends JpaRepository<TemperatureStatistics, Long> {
    Optional<TemperatureStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );

    Optional<TemperatureStatistics> findTopByOrderByCreatedAtDesc();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<DailyStatisticsProjection>
    findByDeviceIdAndStatDateBetweenOrderByStatDate(
            String deviceId,
            LocalDate from,
            LocalDate to
    );
}
