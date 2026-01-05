package com.smarthome.smart_home_iot.batch.step.processor;

import com.smarthome.smart_home_iot.domain.sensor.HumidityStatistics;
import com.smarthome.smart_home_iot.dto.batch.HumidityAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HumidityStatisticsProcessor implements ItemProcessor<HumidityAggResult, HumidityStatistics> {

    @Override
    public HumidityStatistics process(HumidityAggResult item) {

        return HumidityStatistics.builder()
                .deviceId(item.getDeviceId())
                .statDate(item.getStatDate())
                .statHour(item.getStatHour())
                .avgHumidity(item.getAvgHumidity())
                .minHumidity(item.getMinHumidity())
                .maxHumidity(item.getMaxHumidity())
                .sampleCount(item.getSampleCount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
