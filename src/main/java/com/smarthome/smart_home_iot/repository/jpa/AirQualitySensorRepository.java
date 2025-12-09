package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.AirQualitySensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirQualitySensorRepository extends JpaRepository<AirQualitySensor, Long> {
}
