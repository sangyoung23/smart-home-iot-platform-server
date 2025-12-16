package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.PowerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerStatisticsRepository extends JpaRepository<PowerStatistics, Long> {
}
