package com.smarthome.smart_home_iot.dto.dashboard;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashboardRealtimeResponse {

    private String deviceId;

    private String lastUpdatedAt;

    private List<RealtimeSensorStatus> sensors;
}
