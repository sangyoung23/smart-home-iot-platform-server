//package com.smarthome.smart_home_iot.batch.step.reader;
//
//import com.smarthome.smart_home_iot.dto.batch.HumidityAggResult;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.infrastructure.item.ItemReader;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.stereotype.Component;
//
//import java.util.Iterator;
//import java.util.List;
//
//import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
//
//@Component
//@RequiredArgsConstructor
//public class HumidityAggReader implements ItemReader<HumidityAggResult> {
//
//    private final MongoTemplate mongoTemplate;
//    private Iterator<HumidityAggResult> iterator;
//
//    @Override
//    public HumidityAggResult read() {
//        if (iterator == null) {
//            iterator = aggregate().iterator();
//        }
//
//        return iterator.hasNext() ? iterator.next() : null;
//    }
//
//    private List<HumidityAggResult> aggregate() {
//
//        Aggregation aggregation = newAggregation(
//
//                project("deviceId", "humidity", "timestamp")
//                        .andExpression("year(timestamp)").as("year")
//                        .andExpression("month(timestamp").as("month")
//                        .andExpression("dayOfMonth(timestamp)").as("day")
//                        .andExpression("hour(timestamp)").as("hour"),
//
//                group("deviceId", "year", "month", "day", "hour")
//                        .avg("humidity").as("avgHumidity")
//                        .min("humidity").as("minHumidity")
//                        .max("humidity").as("maxHumidity")
//                        .count().as("sampleCount"),
//
//                project("avgHumidity", "minHumidity", "maxHumidity", "sampleCount")
//                        .and("_id.deviceId").as("deviceId")
//                        .andExpression("dateFromParts({ year: _id.year, month: _id})")
//        )
//    }
//}
