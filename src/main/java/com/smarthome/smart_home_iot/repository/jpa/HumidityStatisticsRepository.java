package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.HumidityStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumidityStatisticsRepository extends JpaRepository<HumidityStatistics, Long> {
}
