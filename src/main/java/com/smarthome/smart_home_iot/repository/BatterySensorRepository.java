package com.smarthome.smart_home_iot.repository;

import com.smarthome.smart_home_iot.domain.BatterySensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatterySensorRepository extends JpaRepository<BatterySensor, Long> {
}
