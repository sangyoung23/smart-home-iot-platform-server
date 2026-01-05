package com.smarthome.smart_home_iot.batch.step;

import com.smarthome.smart_home_iot.batch.step.processor.HumidityStatisticsProcessor;
import com.smarthome.smart_home_iot.batch.step.reader.HumidityAggReader;
import com.smarthome.smart_home_iot.batch.step.writer.HumidityStatisticsWriter;
import com.smarthome.smart_home_iot.domain.sensor.HumidityStatistics;
import com.smarthome.smart_home_iot.dto.batch.HumidityAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HumidityStepConfig {

    private final HumidityAggReader humidityReader;
    private final HumidityStatisticsProcessor humidityProcessor;
    private final HumidityStatisticsWriter humidityWriter;

    @Bean
    public Step humidityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        int chunkSize = 100;

        return new ChunkOrientedStepBuilder<HumidityAggResult, HumidityStatistics>("humidityStep", jobRepository, chunkSize)
                .transactionManager(transactionManager)
                .reader(humidityReader)
                .processor(humidityProcessor)
                .writer(humidityWriter)
                .build();
    }
}
