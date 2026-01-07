package com.smarthome.smart_home_iot.dto.dashboard;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseSensorRedisPayload {
    protected String deviceId;
    protected LocalDateTime timestamp;
}

