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

                // 1️⃣ timestamp에서 연/월/일/시간 추출
                project("deviceId", "temperature", "timestamp")
                        .andExpression("year(timestamp)").as("year")
                        .andExpression("month(timestamp)").as("month")
                        .andExpression("dayOfMonth(timestamp)").as("day")
                        .andExpression("hour(timestamp)").as("hour"),

                // 2️⃣ deviceId + 날짜 + 시간 단위로 그룹핑
                group("deviceId", "year", "month", "day", "hour")
                        .avg("temperature").as("avgTemperature") // 평균 온도
                        .min("temperature").as("minTemperature") // 최소 온도
                        .max("temperature").as("maxTemperature") // 최대 온도
                        .count().as("sampleCount"),               // 데이터 개수

                // 3️⃣ 결과 필드 정리 (_id 안의 그룹 키 꺼내기)
                project("avgTemperature", "minTemperature", "maxTemperature", "sampleCount")
                        .and("_id.deviceId").as("deviceId") // 그룹 기준 deviceId
                        .andExpression(
                                "dateFromParts({ year: _id.year, month: _id.month, day: _id.day })"
                        ).as("statDate")                    // 통계 기준 날짜
                        .and("_id.hour").as("statHour")     // 통계 기준 시간
        );

        // Aggregation 실행 후 결과를 DTO로 매핑하여 반환
        return mongoTemplate.aggregate(
                aggregation,               // Aggregation 파이프라인
                "sensor-temperature",      // MongoDB 컬렉션명
                TemperatureAggResult.class // 결과 매핑 DTO
        ).getMappedResults();
    }
}
