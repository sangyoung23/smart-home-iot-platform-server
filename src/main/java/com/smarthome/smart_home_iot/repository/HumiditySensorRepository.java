package com.smarthome.smart_home_iot.repository;

import com.smarthome.smart_home_iot.domain.HumiditySensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumiditySensorRepository extends JpaRepository<HumiditySensor, Long> {
}
