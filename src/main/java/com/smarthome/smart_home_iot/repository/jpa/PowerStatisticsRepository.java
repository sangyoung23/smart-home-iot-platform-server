package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.sensor.DailyStatisticsProjection;
import com.smarthome.smart_home_iot.domain.sensor.PowerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PowerStatisticsRepository extends JpaRepository<PowerStatistics, Long> {
    Optional<PowerStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );

    Optional<PowerStatistics> findTopByOrderByCreatedAtDesc();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<DailyStatisticsProjection>
    findByDeviceIdAndStatDateBetweenOrderByStatDate(
            String deviceId,
            LocalDate from,
            LocalDate to
    );
}
