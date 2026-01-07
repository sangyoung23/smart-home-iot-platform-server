package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureRedisPayload extends BaseSensorRedisPayload {

    private String deviceId;

    private Double temperature;

    private LocalDateTime timestamp;
}
