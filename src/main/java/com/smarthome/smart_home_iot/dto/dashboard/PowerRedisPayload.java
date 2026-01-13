package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PowerRedisPayload extends BaseSensorRedisPayload {

    private String deviceId;

    private Double powerUsage;

    private Double voltage;

    private Double current;

    private Double energyTotal;

    private String timestamp;
}
