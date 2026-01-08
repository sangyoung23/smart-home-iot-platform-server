package com.smarthome.smart_home_iot.dto.dashboard;

import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class DashboardStatsResponse {

    private String deviceId;
    private SensorType sensorType;
    private List<SensorStatsDto> data;
}
