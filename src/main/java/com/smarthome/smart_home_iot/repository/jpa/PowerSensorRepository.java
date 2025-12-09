package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.PowerSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerSensorRepository extends JpaRepository<PowerSensor, Long> {
}
