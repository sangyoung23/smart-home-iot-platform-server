package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.PowerAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@RequiredArgsConstructor
public class PowerAggReader implements ItemReader<PowerAggResult> {

    private final MongoTemplate mongoTemplate;
    private Iterator<PowerAggResult> iterator;

    @Override
    public PowerAggResult read() {
        if (iterator == null) {
            iterator = aggregate().iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<PowerAggResult> aggregate() {

        Aggregation aggregation = newAggregation(

                project("deviceId", "powerUsage", "voltage", "current", "energy", "timestamp")
                        .andExpression("year(timestamp)").as("year")
                        .andExpression("month(timestamp)").as("month")
                        .andExpression("dayOfMonth(timestamp)").as("day")
                        .andExpression("hour(timestamp)").as("hour"),

                group("deviceId", "year", "month", "day", "hour")
                        .avg("powerUsage").as("avgPowerUsage")
                        .min("powerUsage").as("minPowerUsage")
                        .max("powerUsage").as("maxPowerUsage")
                        .avg("voltage").as("avgVoltage")
                        .avg("current").as("avgCurrent")
                        .sum("energy").as("totalEnergy")
                        .count().as("sampleCount"),

                project(
                        "avgPowerUsage",
                        "minPowerUsage",
                        "maxPowerUsage",
                        "avgVoltage",
                        "avgCurrent",
                        "totalEnergy",
                        "sampleCount"
                )
                        .and("_id.deviceId").as("deviceId")
                        .andExpression(
                                "dateFromParts({ year: _id.year, month: _id.month, day: _id.day })"
                        ).as("statDate")
                        .and("_id.hour").as("statHour")
        );

        return mongoTemplate.aggregate(
                aggregation,
                "sensor-power",
                PowerAggResult.class
        ).getMappedResults();
    }
}
