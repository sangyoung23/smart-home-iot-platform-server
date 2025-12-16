package com.smarthome.smart_home_iot.batch.job;

import com.smarthome.smart_home_iot.batch.step.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final TemperatureStepConfig temperatureStepConfig;
//    private final HumidityStepConfig humidityStepConfig;
//    private final PowerStepConfig powerStepConfig;
//    private final BatteryStepConfig batteryStepConfig;
//    private final AirQualityStepConfig airQualityStepConfig;

    @Bean
    public Job sensorJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("sensorJob", jobRepository)
                .start(temperatureStepConfig.temperatureStep(jobRepository, transactionManager))
//                .next(humidityStepConfig.humidityStep(jobRepository, transactionManager))
//                .next(powerStepConfig.powerStep(jobRepository, transactionManager))
//                .next(batteryStepConfig.batteryStep(jobRepository, transactionManager))
//                .next(airQualityStepConfig.airQualityStep(jobRepository, transactionManager))
                .build();
    }
}
