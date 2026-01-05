package com.smarthome.smart_home_iot.batch.step.processor;

import com.smarthome.smart_home_iot.domain.sensor.AirQualityStatistics;
import com.smarthome.smart_home_iot.dto.batch.AirQualityAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AirQualityStatisticsProcessor
        implements ItemProcessor<AirQualityAggResult, AirQualityStatistics> {

    @Override
    public AirQualityStatistics process(AirQualityAggResult item) {

        return AirQualityStatistics.builder()
                .deviceId(item.getDeviceId())
                .statDate(item.getStatDate())
                .statHour(item.getStatHour())

                .avgPm10(item.getAvgPm10())
                .avgPm25(item.getAvgPm25())
                .avgCo2(item.getAvgCo2())
                .avgVoc(item.getAvgVoc())
                .avgLight(item.getAvgLight())

                .gasLeakCount(item.getGasLeakCount())
                .avgSmokeLevel(item.getAvgSmokeLevel())
                .sampleCount(item.getSampleCount())

                .createdAt(LocalDateTime.now())
                .build();
    }
}
