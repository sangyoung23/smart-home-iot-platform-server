package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.BatteryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryStatisticsRepository extends JpaRepository<BatteryStatistics, Long> {
}
