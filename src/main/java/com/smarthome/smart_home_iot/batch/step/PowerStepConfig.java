package com.smarthome.smart_home_iot.batch.step;

import com.smarthome.smart_home_iot.batch.step.processor.PowerStatisticsProcessor;
import com.smarthome.smart_home_iot.batch.step.reader.PowerAggReader;
import com.smarthome.smart_home_iot.batch.step.writer.PowerStatisticsWriter;
import com.smarthome.smart_home_iot.domain.sensor.PowerStatistics;
import com.smarthome.smart_home_iot.dto.batch.PowerAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class PowerStepConfig {

    private final PowerAggReader powerReader;
    private final PowerStatisticsProcessor powerProcessor;
    private final PowerStatisticsWriter powerWriter;

    @Bean
    public Step powerStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager) {

        int chunkSize = 100;

        return new ChunkOrientedStepBuilder<PowerAggResult, PowerStatistics>(
                "powerStep",
                jobRepository,
                chunkSize
        )
                .transactionManager(transactionManager)
                .reader(powerReader)
                .processor(powerProcessor)
                .writer(powerWriter)
                .build();
    }
}