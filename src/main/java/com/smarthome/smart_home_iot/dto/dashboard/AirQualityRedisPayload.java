package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityRedisPayload extends BaseSensorRedisPayload {

    private String deviceId;

    private Double pm10;
    private Double pm25;

    private Integer co2;
    private Integer voc;
    private Integer light;
    private Integer smokeLevel;

    private Boolean gasLeak;

    private String timestamp;
}
