package com.smarthome.smart_home_iot.batch.step;

import com.smarthome.smart_home_iot.batch.step.processor.AirQualityStatisticsProcessor;
import com.smarthome.smart_home_iot.batch.step.reader.AirQualityAggReader;
import com.smarthome.smart_home_iot.batch.step.writer.AirQualityStatisticsWriter;
import com.smarthome.smart_home_iot.domain.sensor.AirQualityStatistics;
import com.smarthome.smart_home_iot.dto.batch.AirQualityAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AirQualityStepConfig {

    private final AirQualityAggReader airQualityReader;
    private final AirQualityStatisticsProcessor airQualityProcessor;
    private final AirQualityStatisticsWriter airQualityWriter;

    @Bean
    public Step airQualityStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        int chunkSize = 100;

        return new ChunkOrientedStepBuilder<AirQualityAggResult, AirQualityStatistics>("airQualityStep", jobRepository, chunkSize)
                .transactionManager(transactionManager)
                .reader(airQualityReader)
                .processor(airQualityProcessor)
                .writer(airQualityWriter)
                .build();
    }
}
