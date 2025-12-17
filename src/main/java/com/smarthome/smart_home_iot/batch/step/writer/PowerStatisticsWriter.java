package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.PowerStatistics;
import com.smarthome.smart_home_iot.repository.jpa.PowerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PowerStatisticsWriter implements ItemWriter<PowerStatistics> {

    private final PowerStatisticsRepository repository;

    @Override
    public void write(Chunk<? extends PowerStatistics> chunk) {
        repository.saveAll(chunk.getItems());
    }
}