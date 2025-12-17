package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.HumidityStatistics;
import com.smarthome.smart_home_iot.repository.jpa.HumidityStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HumidityStatisticsWriter implements ItemWriter<HumidityStatistics> {

    private final HumidityStatisticsRepository repository;

    @Override
    public void write(Chunk<? extends HumidityStatistics> chunk) {
        repository.saveAll(chunk.getItems());
    }
}
