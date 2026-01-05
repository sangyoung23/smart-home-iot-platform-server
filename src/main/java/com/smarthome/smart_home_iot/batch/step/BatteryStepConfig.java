package com.smarthome.smart_home_iot.batch.step;

import com.smarthome.smart_home_iot.batch.step.processor.BatteryStatisticsProcessor;
import com.smarthome.smart_home_iot.batch.step.reader.BatteryAggReader;
import com.smarthome.smart_home_iot.batch.step.writer.BatteryStatisticsWriter;
import com.smarthome.smart_home_iot.domain.sensor.BatteryStatistics;
import com.smarthome.smart_home_iot.dto.batch.BatteryAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatteryStepConfig {

    private final BatteryAggReader batteryReader;
    private final BatteryStatisticsProcessor batteryProcessor;
    private final BatteryStatisticsWriter batteryWriter;

    @Bean
    public Step batteryStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager) {

        int chunkSize = 100;

        return new ChunkOrientedStepBuilder<BatteryAggResult, BatteryStatistics>(
                "batteryStep",
                jobRepository,
                chunkSize
        )
                .transactionManager(transactionManager)
                .reader(batteryReader)
                .processor(batteryProcessor)
                .writer(batteryWriter)
                .build();
    }
}