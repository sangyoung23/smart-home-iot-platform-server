package com.smarthome.smart_home_iot.batch.scheduler;

import com.smarthome.smart_home_iot.repository.mongo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorJobScheduler {

    @Autowired
    JobOperator jobOperator;

    private final Job sensorJob;

    private final TemperatureDocumentRepository temperatureRepo;
    private final HumidityDocumentRepository humidityRepo;
    private final PowerDocumentRepository powerRepo;
    private final BatteryDocumentRepository batteryRepo;
    private final AirQualityDocumentRepository airQualityRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void runSensorJob() {

        if (!hasBatchData()) {
            log.info("배치 실행 대상 데이터 없음, 스킵");
            return;
        }

        try {
            log.info("센서 데이터 Job 실행");

            jobOperator.start(sensorJob, new JobParameters());

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