package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.BatteryAggResult;
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
public class BatteryAggReader implements ItemReader<BatteryAggResult> {

    private final MongoTemplate mongoTemplate;
    private Iterator<BatteryAggResult> iterator;

    @Override
    public BatteryAggResult read() {
        if (iterator == null) {
            iterator = aggregate().iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<BatteryAggResult> aggregate() {

        Aggregation aggregation = newAggregation(

                project("deviceId", "battery", "timestamp")
                        .andExpression("year(timestamp)").as("year")
                        .andExpression("month(timestamp)").as("month")
                        .andExpression("dayOfMonth(timestamp)").as("day")
                        .andExpression("hour(timestamp)").as("hour"),

                group("deviceId", "year", "month", "day", "hour")
                        .avg("battery").as("avgBattery")
                        .min("battery").as("minBattery")
                        .max("battery").as("maxBattery")
                        .count().as("sampleCount"),

                project("avgBattery", "minBattery", "maxBattery", "sampleCount")
                        .and("_id.deviceId").as("deviceId")
                        .andExpression(
                                "dateFromParts({ year: _id.year, month: _id.month, day: _id.day })"
                        ).as("statDate")
                        .and("_id.hour").as("statHour")
        );

        return mongoTemplate.aggregate(
                aggregation,
                "sensor-battery",
                BatteryAggResult.class
        ).getMappedResults();
    }
}