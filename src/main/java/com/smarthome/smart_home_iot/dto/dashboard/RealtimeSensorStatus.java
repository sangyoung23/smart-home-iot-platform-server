package com.smarthome.smart_home_iot.dto.dashboard;

import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealtimeSensorStatus {

    private SensorType sensorType;

    private Double currentValue;

    private String lastDataTime;

    private boolean normal;

    private String statusMessage;
}
