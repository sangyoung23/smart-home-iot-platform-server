package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.TemperatureAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@RequiredArgsConstructor
public class TemperatureAggReader implements ItemReader<TemperatureAggResult> {

    // MongoDB Aggregation 실행을 위한 템플릿
    private final MongoTemplate mongoTemplate;
    // Aggregation 결과를 하나씩 꺼내기 위한 Iterator
    private Iterator<TemperatureAggResult> iterator;

    @Override
    public TemperatureAggResult read() {
        // Step 실행 중 최초 1회만 Aggregation 수행
        if (iterator == null) {
            iterator = aggregate().iterator();
        }
        // 데이터가 있으면 1건 반환, 없으면 null → Batch 종료 신호
        return iterator.hasNext() ? iterator.next() : null;
    }

    // MongoDB Aggregation 실제 수행 메서드
    private List<TemperatureAggResult> aggregate() {

        Aggregation aggregation = newAggregation(

                project("deviceId", "temperature", "timestamp")
                        .and(
                                DateOperators.Year
                                        .yearOf("timestamp")
                                        .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))
                        ).as("year")
                        .and(
                                DateOperators.Month
                                        .monthOf("timestamp")
                                        .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))
                        ).as("month")
                        .and(
                                DateOperators.DayOfMonth
                                        .dayOfMonth("timestamp")
                                        .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))
                        ).as("day")
                        .and(
                                DateOperators.Hour
                                        .hourOf("timestamp")
                                        .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul"))
                        ).as("hour"),

        group("deviceId", "year", "month", "day", "hour")
                        .avg("temperature").as("avgTemperature")
                        .min("temperature").as("minTemperature")
                        .max("temperature").as("maxTemperature")
                        .count().as("sampleCount"),

                project("avgTemperature", "minTemperature", "maxTemperature", "sampleCount")
                        .and("_id.deviceId").as("deviceId")
                        .and(
                                DateOperators.DateFromParts
                                        .dateFromParts()
                                        .year("$_id.year")
                                        .month("$_id.month")
                                        .day("$_id.day")
                        ).as("statDate")
                        .and("_id.hour").as("statHour")
                        .andExclude("_id")
        );

        // Aggregation 실행 후 결과를 DTO로 매핑하여 반환
        return mongoTemplate.aggregate(
                aggregation,
                "sensor-temperature",
                TemperatureAggResult.class
        ).getMappedResults();
    }
}
