package com.smarthome.smart_home_iot.batch.step;

import com.smarthome.smart_home_iot.batch.step.processor.TemperatureStatisticsProcessor;
import com.smarthome.smart_home_iot.batch.step.reader.TemperatureAggReader;
import com.smarthome.smart_home_iot.batch.step.writer.TemperatureStatisticsWriter;
import com.smarthome.smart_home_iot.domain.sensor.TemperatureStatistics;
import com.smarthome.smart_home_iot.dto.batch.TemperatureAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TemperatureStepConfig {

    private final TemperatureAggReader temperatureReader;
    private final TemperatureStatisticsProcessor temperatureProcessor;
    private final TemperatureStatisticsWriter temperatureWriter;

    @Bean
    public Step temperatureStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        int chunkSize = 100;

        return new ChunkOrientedStepBuilder<TemperatureAggResult, TemperatureStatistics>("temperatureStep", jobRepository, chunkSize)
                .transactionManager(transactionManager)
                .reader(temperatureReader)
                .processor(temperatureProcessor)
                .writer(temperatureWriter)
                .build();
    }
}
