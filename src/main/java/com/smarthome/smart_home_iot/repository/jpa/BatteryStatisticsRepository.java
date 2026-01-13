package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.sensor.BatteryStatistics;
import com.smarthome.smart_home_iot.domain.sensor.DailyStatisticsProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BatteryStatisticsRepository extends JpaRepository<BatteryStatistics, Long> {
    Optional<BatteryStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );

    Optional<BatteryStatistics> findTopByOrderByCreatedAtDesc();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<DailyStatisticsProjection>
    findByDeviceIdAndStatDateBetweenOrderByStatDate(
            String deviceId,
            LocalDate from,
            LocalDate to
    );
}
