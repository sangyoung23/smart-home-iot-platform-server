package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.TemperatureStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureStatisticsRepository extends JpaRepository<TemperatureStatistics, Long> {
}
