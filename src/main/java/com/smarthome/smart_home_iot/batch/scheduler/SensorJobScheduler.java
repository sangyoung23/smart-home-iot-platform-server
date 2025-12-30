package com.smarthome.smart_home_iot.batch.scheduler;

import com.smarthome.smart_home_iot.repository.mongo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job sensorJob;

    private final TemperatureDocumentRepository temperatureRepo;
    private final HumidityDocumentRepository humidityRepo;
    private final PowerDocumentRepository powerRepo;
    private final BatteryDocumentRepository batteryRepo;
    private final AirQualityDocumentRepository airQualityRepo;

    @Scheduled(cron = "0 * * * * *")  // 매분마다 (테스트용)
    public void runSensorJob() {

        // 1️⃣ 처리할 데이터 없으면 스킵
        if (!hasBatchData()) {
            log.info("배치 실행 대상 데이터 없음, 스킵");
            return;
        }

        try {
            log.info("센서 데이터 Job 실행");

            JobParameters params = new JobParametersBuilder()
                    .addLong("run.id", System.currentTimeMillis()) // JobInstance 구분용
                    .toJobParameters();

            jobLauncher.run(sensorJob, params);

            log.info("센서 데이터 Job 종료");

        } catch (Exception e) {
            log.error("센서 데이터 Job 실패", e);
        }
    }

    private boolean hasBatchData() {
        return temperatureRepo.existsByIsProcessedFalse()
                || humidityRepo.existsByIsProcessedFalse()
                || powerRepo.existsByIsProcessedFalse()
                || batteryRepo.existsByIsProcessedFalse()
                || airQualityRepo.existsByIsProcessedFalse();
    }
}