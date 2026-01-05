package com.smarthome.smart_home_iot.batch.step.processor;

import com.smarthome.smart_home_iot.domain.sensor.TemperatureStatistics;
import com.smarthome.smart_home_iot.dto.batch.TemperatureAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TemperatureStatisticsProcessor implements ItemProcessor<TemperatureAggResult, TemperatureStatistics> {

    @Override
    public TemperatureStatistics process(TemperatureAggResult item) {

        return TemperatureStatistics.builder()
                .deviceId(item.getDeviceId())
                .statDate(item.getStatDate())
                .statHour(item.getStatHour())
                .avgTemperature(item.getAvgTemperature())
                .minTemperature(item.getMinTemperature())
                .maxTemperature(item.getMaxTemperature())
                .sampleCount(item.getSampleCount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
