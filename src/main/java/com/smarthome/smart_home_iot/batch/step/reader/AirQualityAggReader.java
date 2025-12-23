package com.smarthome.smart_home_iot.batch.step.reader;

import com.smarthome.smart_home_iot.dto.batch.AirQualityAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@RequiredArgsConstructor
public class AirQualityAggReader implements ItemReader<AirQualityAggResult> {

    private final MongoTemplate mongoTemplate;
    private Iterator<AirQualityAggResult> iterator;

    @Override
    public AirQualityAggResult read() {
        if (iterator == null) {
            iterator = aggregate().iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<AirQualityAggResult> aggregate() {

        Aggregation aggregation = newAggregation(

                match(Criteria.where("isProcessed").is(false)),

                // 1️⃣ timestamp → 연/월/일/시간 추출
                project("deviceId", "pm10", "pm25", "co2", "voc", "light", "smokeLevel", "gasLeak", "timestamp")
                        .and(DateOperators.Year
                                .yearOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("year")
                        .and(DateOperators.Month
                                .monthOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("month")
                        .and(DateOperators.DayOfMonth
                                .dayOfMonth("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("day")
                        .and(DateOperators.Hour
                                .hourOf("timestamp")
                                .withTimezone(DateOperators.Timezone.valueOf("Asia/Seoul")))
                        .as("hour")
                ,

                // 2️⃣ device + 날짜 + 시간 기준 그룹핑
                group("deviceId", "year", "month", "day", "hour")
                        .avg("pm10").as("avgPm10")
                        .avg("pm25").as("avgPm25")
                        .avg("co2").as("avgCo2")
                        .avg("voc").as("avgVoc")
                        .avg("light").as("avgLight")
                        .avg("smokeLevel").as("avgSmokeLevel")
                        .sum("gasLeak").as("gasLeakCount")
                        .count().as("sampleCount"),

                // 3️⃣ 결과 필드 정리
                project(
                        "avgPm10", "avgPm25", "avgCo2", "avgVoc",
                        "avgLight", "avgSmokeLevel", "gasLeakCount", "sampleCount"
                )
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

        return mongoTemplate.aggregate(
                aggregation,
                "sensor-air-quality",
                AirQualityAggResult.class
        ).getMappedResults();
    }
}
