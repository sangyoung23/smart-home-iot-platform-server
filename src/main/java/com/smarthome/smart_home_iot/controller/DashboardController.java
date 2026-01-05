package com.smarthome.smart_home_iot.controller;

import com.smarthome.smart_home_iot.dto.dashboard.DashboardSummaryResponse;
import com.smarthome.smart_home_iot.service.DashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardSummaryService dashboardSummaryService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardSummaryService.getSummary();
    }
}
