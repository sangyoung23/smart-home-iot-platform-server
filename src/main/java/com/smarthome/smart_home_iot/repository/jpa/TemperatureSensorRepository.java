package com.smarthome.smart_home_iot.repository.jpa;

import com.smarthome.smart_home_iot.domain.TemperatureSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureSensorRepository extends JpaRepository<TemperatureSensor, Long> {
}
