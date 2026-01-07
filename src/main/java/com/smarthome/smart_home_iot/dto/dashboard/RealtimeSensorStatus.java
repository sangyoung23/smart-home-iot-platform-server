package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RealtimeSensorStatus {

    private SensorType sensorType;

    private Double currentValue;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime lastDataTime;

    private boolean normal;

    private String statusMessage;
}
