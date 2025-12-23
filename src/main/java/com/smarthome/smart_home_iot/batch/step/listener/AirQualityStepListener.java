package com.smarthome.smart_home_iot.batch.step.listener;

import com.smarthome.smart_home_iot.document.AirQualityDocument;
import com.smarthome.smart_home_iot.document.BatteryDocument;
import com.smarthome.smart_home_iot.repository.mongo.AirQualityDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AirQualityStepListener implements StepExecutionListener {

    private final AirQualityDocumentRepository airQualityDocumentRepository;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LocalDateTime batchStartTime = stepExecution
                .getJobExecution()
                .getJobParameters()
                .getLocalDateTime("batchStartTime");

        LocalDateTime batchEndTime = LocalDateTime.now();

        if (batchStartTime != null) {
            // 데이터 조회
            List<AirQualityDocument> documents = airQualityDocumentRepository
                    .findByCreatedAtBetweenAndIsProcessedFalse(batchStartTime, batchEndTime);

            if (!documents.isEmpty()) {
                // 모든 데이터를 isProcessed = true로 변환
                List<AirQualityDocument> updatedDocuments = documents.stream()
                        .map(doc -> doc.toBuilder()
                                .isProcessed(true)
                                .build())
                        .collect(Collectors.toList());

                // 일괄 저장 (한 번에 저장)
                airQualityDocumentRepository.saveAll(updatedDocuments);

                log.info("AirQuality {} 개 처리됨", documents.size());
            } else {
                log.info("처리할 AirQuality 데이터 없음");
            }
        }

        return ExitStatus.COMPLETED;
    }
}
