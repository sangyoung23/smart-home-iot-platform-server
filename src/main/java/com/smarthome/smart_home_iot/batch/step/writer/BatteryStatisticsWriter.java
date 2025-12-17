package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.BatteryStatistics;
import com.smarthome.smart_home_iot.repository.jpa.BatteryStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatteryStatisticsWriter implements ItemWriter<BatteryStatistics> {

    private final BatteryStatisticsRepository repository;

    @Override
    public void write(Chunk<? extends BatteryStatistics> chunk) {
        repository.saveAll(chunk.getItems());
    }
}