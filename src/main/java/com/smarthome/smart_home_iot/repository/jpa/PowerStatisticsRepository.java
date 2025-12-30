package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.PowerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PowerStatisticsRepository extends JpaRepository<PowerStatistics, Long> {
    Optional<PowerStatistics> findByDeviceIdAndStatDateAndStatHour(
            String deviceId,
            LocalDate statDate,
            int statHour
    );
}
