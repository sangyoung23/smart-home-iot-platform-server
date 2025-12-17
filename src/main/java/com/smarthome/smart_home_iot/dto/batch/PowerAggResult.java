package com.smarthome.smart_home_iot.dto.batch;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PowerAggResult {

    private String deviceId;
    private LocalDate statDate;
    private int statHour;

    private double avgPowerUsage;
    private double minPowerUsage;
    private double maxPowerUsage;

    private double avgVoltage;
    private double avgCurrent;

    private double totalEnergy;

    private int sampleCount;
}
