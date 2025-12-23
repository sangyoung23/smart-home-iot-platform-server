package com.smarthome.smart_home_iot.batch.step.listener;

import com.smarthome.smart_home_iot.document.HumidityDocument;
import com.smarthome.smart_home_iot.document.PowerDocument;
import com.smarthome.smart_home_iot.repository.mongo.HumidityDocumentRepository;
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
public class HumidityStepListener implements StepExecutionListener {

    private final HumidityDocumentRepository humidityDocumentRepository;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LocalDateTime batchStartTime = stepExecution
                .getJobExecution()
                .getJobParameters()
                .getLocalDateTime("batchStartTime");

        LocalDateTime batchEndTime = LocalDateTime.now();

        if (batchStartTime != null) {
            // 데이터 조회
            List<HumidityDocument> documents = humidityDocumentRepository
                    .findByCreatedAtBetweenAndIsProcessedFalse(batchStartTime, batchEndTime);

            if (!documents.isEmpty()) {
                // 모든 데이터를 isProcessed = true로 변환
                List<HumidityDocument> updatedDocuments = documents.stream()
                        .map(doc -> doc.toBuilder()
                                .isProcessed(true)
                                .build())
                        .collect(Collectors.toList());

                // 일괄 저장 (한 번에 저장)
                humidityDocumentRepository.saveAll(updatedDocuments);

                log.info("Humidity {} 개 처리됨", documents.size());
            } else {
                log.info("처리할 Humidity 데이터 없음");
            }
        }

        return ExitStatus.COMPLETED;
    }
}
