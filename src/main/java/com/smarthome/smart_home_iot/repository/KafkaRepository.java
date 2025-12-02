package com.smarthome.smart_home_iot.repository;

import com.smarthome.smart_home_iot.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaRepository extends JpaRepository<Sensor, Long> {

}
