package com.smarthome.smart_home_iot.dto.batch;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class AirQualityAggResult {

    private String deviceId;

    private LocalDate statDate;

    private int statHour;

    private double avgPm10;

    private double avgPm25;

    private double avgCo2;

    private double avgVoc;

    private double avgLight;

    private int gasLeakCount;

    private double avgSmokeLevel;

    private int sampleCount;

    private List<ObjectId> docIds;
}
