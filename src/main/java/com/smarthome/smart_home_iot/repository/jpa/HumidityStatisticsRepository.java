package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.HumidityStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HumidityStatisticsRepository extends JpaRepository<HumidityStatistics, Long> {
    Optional<HumidityStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );
}
