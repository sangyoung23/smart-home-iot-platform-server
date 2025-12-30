package com.smarthome.smart_home_iot.dto.batch;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class BatteryAggResult {

    private String deviceId;
    private LocalDate statDate;
    private int statHour;

    private double avgBattery;
    private int minBattery;
    private int maxBattery;

    private int sampleCount;

    private List<ObjectId> docIds;
}