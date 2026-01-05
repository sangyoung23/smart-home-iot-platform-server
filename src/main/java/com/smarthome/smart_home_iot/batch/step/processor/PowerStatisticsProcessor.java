package com.smarthome.smart_home_iot.batch.step.processor;

import com.smarthome.smart_home_iot.domain.sensor.PowerStatistics;
import com.smarthome.smart_home_iot.dto.batch.PowerAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PowerStatisticsProcessor
        implements ItemProcessor<PowerAggResult, PowerStatistics> {

    @Override
    public PowerStatistics process(PowerAggResult item) {

        return PowerStatistics.builder()
                .deviceId(item.getDeviceId())
                .statDate(item.getStatDate())
                .statHour(item.getStatHour())
                .avgPowerUsage(item.getAvgPowerUsage())
                .minPowerUsage(item.getMinPowerUsage())
                .maxPowerUsage(item.getMaxPowerUsage())
                .avgVoltage(item.getAvgVoltage())
                .avgCurrent(item.getAvgCurrent())
                .totalEnergy(item.getTotalEnergy())
                .sampleCount(item.getSampleCount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}