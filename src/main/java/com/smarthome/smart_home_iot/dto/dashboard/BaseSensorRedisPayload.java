package com.smarthome.smart_home_iot.dto.dashboard;

import lombok.Getter;

@Getter
public abstract class BaseSensorRedisPayload {
    protected String deviceId;
    protected String timestamp;
}

