package com.smarthome.smart_home_iot.dto.dashboard;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter @Builder
public class SensorStatsDto {

    private LocalDate stat_date;

    private Double avg_value;
}
