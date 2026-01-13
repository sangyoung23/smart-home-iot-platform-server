package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatteryRedisPayload extends BaseSensorRedisPayload {

    private String deviceId;

    private Double battery;

    private String timestamp;
}
