package com.smarthome.smart_home_iot.dto.batch;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class HumidityAggResult {
    private String deviceId;

    private LocalDate statDate;

    private int statHour;

    private double avgHumidity;

    private double minHumidity;

    private double maxHumidity;

    private int sampleCount;

    private List<ObjectId> docIds;
}
