package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.PowerAggResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PowerAggReader implements ItemReader<PowerAggResult> {

    private final MongoTemplate mongoTemplate;
    private Iterator<PowerAggResult> iterator;

    private List<ObjectId> processedIds = new ArrayList<>();

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        iterator = null;

        processedIds = new ArrayList<>();

        stepExecution
                .getExecutionContext()
                .put("processedPowerIds", processedIds);
    }

    @Override
    public PowerAggResult read() {
        if (iterator == null) {
            List<PowerAggResult> results = aggregate();
            iterator = results.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<PowerAggResult> aggregate() {

        ZoneId zone = ZoneId.of("Asia/Seoul");

        LocalDateTime todayStart =
                LocalDateTime.now(zone)
                        .toLocalDate()
                        .atStartOfDay();

        Aggregation aggregation = newAggregation(
                match(Criteria
                        .where("timestamp").lt(todayStart)
                        .and("isProcessed").is(false)
                ),

                project("deviceId", "powerUsage", "voltage", "current", "energy", "timestamp")
                        .and("_id").as("docId")
                        .and(DateOperators.Year.yearOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("year")
                        .and(DateOperators.Month.monthOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("month")
                        .and(DateOperators.DayOfMonth.dayOfMonth("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("day")
                        .and(DateOperators.Hour.hourOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("hour"),

                group("deviceId", "year", "month", "day", "hour")
                        .avg("powerUsage").as("avgPowerUsage")
                        .min("powerUsage").as("minPowerUsage")
                        .max("powerUsage").as("maxPowerUsage")
                        .avg("voltage").as("avgVoltage")
                        .avg("current").as("avgCurrent")
                        .sum("energy").as("totalEnergy")
                        .count().as("sampleCount")
                        .addToSet("docId").as("docIds"),

                project(
                        "avgPowerUsage", "minPowerUsage", "maxPowerUsage",
                        "avgVoltage", "avgCurrent", "totalEnergy", "sampleCount"
                )
                        .and("_id.deviceId").as("deviceId")
                        .and("_id.hour").as("statHour")
                        .and(
                                DateOperators.DateFromParts.dateFromParts()
                                        .year("$_id.year")
                                        .month("$_id.month")
                                        .day("$_id.day")
                        ).as("statDate")
                        .and("docIds").as("docIds")
                        .andExclude("_id")
        );

        List<PowerAggResult> results =
                mongoTemplate.aggregate(
                        aggregation,
                        "sensor-power",
                        PowerAggResult.class
                ).getMappedResults();

        results.forEach(r ->
                r.getDocIds().forEach(id -> processedIds.add(id))
        );

        log.info("PowerAggReader - 집계 결과 개수: {}", results.size());

        return results;
    }
}
