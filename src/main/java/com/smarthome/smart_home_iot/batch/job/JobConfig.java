package com.smarthome.smart_home_iot.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JobConfig {

    private final Step temperatureStep;
    private final Step humidityStep;
    private final Step powerStep;
    private final Step batteryStep;
    private final Step airQualityStep;

    @Bean
    public Job sensorJob(JobRepository jobRepository) {
        return new JobBuilder("sensorJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(temperatureStep)
                .next(humidityStep)
                .next(powerStep)
                .next(batteryStep)
                .next(airQualityStep)
                .build();
    }
}

