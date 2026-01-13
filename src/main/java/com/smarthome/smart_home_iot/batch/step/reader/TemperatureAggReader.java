package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.TemperatureAggResult;
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
public class TemperatureAggReader implements ItemReader<TemperatureAggResult> {

    private final MongoTemplate mongoTemplate;
    private Iterator<TemperatureAggResult> iterator;

    private List<ObjectId> processedIds = new ArrayList<>();

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        iterator = null;

        processedIds = new ArrayList<>();

        stepExecution
                .getExecutionContext()
                .put("processedTemperatureIds", processedIds);
    }

    @Override
    public TemperatureAggResult read() {
        if (iterator == null) {
            List<TemperatureAggResult> results = aggregate();
            iterator = results.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<TemperatureAggResult> aggregate() {

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

                project("deviceId", "temperature", "timestamp")
                        .and("_id").as("docId")
                        .and(DateOperators.Year.yearOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))).as("year")
                        .and(DateOperators.Month.monthOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))).as("month")
                        .and(DateOperators.DayOfMonth.dayOfMonth("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))).as("day")
                        .and(DateOperators.Hour.hourOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))).as("hour"),

                group("deviceId", "year", "month", "day", "hour")
                        .avg("temperature").as("avgTemperature")
                        .min("temperature").as("minTemperature")
                        .max("temperature").as("maxTemperature")
                        .count().as("sampleCount")
                        .addToSet("docId").as("docIds"),

                project("avgTemperature", "minTemperature", "maxTemperature", "sampleCount")
                        .and("_id.deviceId").as("deviceId")
                        .and("_id.hour").as("statHour")
                        .and(
                                DateOperators.DateFromParts
                                        .dateFromParts()
                                        .year("$_id.year")
                                        .month("$_id.month")
                                        .day("$_id.day")
                        ).as("statDate")
                        .and("docIds").as("docIds")
                        .andExclude("_id")
        );

        List<TemperatureAggResult> results =
                mongoTemplate.aggregate(
                        aggregation,
                        "sensor-temperature",
                        TemperatureAggResult.class
                ).getMappedResults();

        results.forEach(r ->
                r.getDocIds().forEach(id -> processedIds.add(id))
        );

        log.info("TemperatureAggReader - 집계 결과 개수: {}", results.size());

        return results;
    }
}

