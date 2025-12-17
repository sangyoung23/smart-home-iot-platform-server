package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.AirQualityStatistics;
import com.smarthome.smart_home_iot.repository.jpa.AirQualityStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirQualityStatisticsWriter implements ItemWriter<AirQualityStatistics> {

    private final AirQualityStatisticsRepository repository;

    @Override
    public void write(Chunk<? extends AirQualityStatistics> chunk) {
        repository.saveAll(chunk.getItems());
    }
}
