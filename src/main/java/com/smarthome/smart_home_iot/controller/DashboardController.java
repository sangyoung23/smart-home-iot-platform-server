package com.smarthome.smart_home_iot.controller;

import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardRealtimeResponse;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardStatsResponse;
import com.smarthome.smart_home_iot.dto.dashboard.DashboardSummaryResponse;
import com.smarthome.smart_home_iot.service.DashboardRealtimeService;
import com.smarthome.smart_home_iot.service.DashboardStatsService;
import com.smarthome.smart_home_iot.service.DashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardSummaryService dashboardSummaryService;
    private final DashboardRealtimeService dashboardRealtimeService;
    private final DashboardStatsService dashboardStatsService;

    // 대시보드 센서 요약 데이터 조회
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardSummaryService.getSummary();
    }

    // 대시보드 센서 실시간 조회
    @GetMapping("/realtime")
    public DashboardRealtimeResponse getRealtime(@RequestParam String deviceId) {
        return dashboardRealtimeService.getRealtime(deviceId);
    }

    // 센서 상세 통계 조회
    @GetMapping("/stats")
    public DashboardStatsResponse getStats(
            @RequestParam SensorType sensorType,
            @RequestParam String deviceId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
            ) {
        return dashboardStatsService.getStats(sensorType, deviceId, from, to);
    }
}
