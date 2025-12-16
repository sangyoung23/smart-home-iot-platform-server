package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.TemperatureStatistics;
import com.smarthome.smart_home_iot.repository.jpa.TemperatureStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemperatureStatisticsWriter implements ItemWriter<TemperatureStatistics> {

    private final TemperatureStatisticsRepository repository;

    @Override
    public void write(Chunk<? extends TemperatureStatistics> chunk) {
        repository.saveAll(chunk.getItems());
    }
}
