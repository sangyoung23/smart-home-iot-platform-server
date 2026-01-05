package com.smarthome.smart_home_iot.batch.step.processor;

import com.smarthome.smart_home_iot.domain.sensor.BatteryStatistics;
import com.smarthome.smart_home_iot.dto.batch.BatteryAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BatteryStatisticsProcessor
        implements ItemProcessor<BatteryAggResult, BatteryStatistics> {

    @Override
    public BatteryStatistics process(BatteryAggResult item) {

        return BatteryStatistics.builder()
                .deviceId(item.getDeviceId())
                .statDate(item.getStatDate())
                .statHour(item.getStatHour())
                .avgBattery(item.getAvgBattery())
                .minBattery(item.getMinBattery())
                .maxBattery(item.getMaxBattery())
                .sampleCount(item.getSampleCount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}