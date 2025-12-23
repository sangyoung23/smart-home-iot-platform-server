package com.smarthome.smart_home_iot.batch.step.listener;

import com.smarthome.smart_home_iot.document.TemperatureDocument;
import com.smarthome.smart_home_iot.repository.mongo.TemperatureDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class TemperatureStepListener implements StepExecutionListener {

    private final TemperatureDocumentRepository temperatureDocumentRepository;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LocalDateTime batchStartTime = stepExecution
                .getJobExecution()
                .getJobParameters()
                .getLocalDateTime("batchStartTime");

        LocalDateTime batchEndTime = LocalDateTime.now();

        if (batchStartTime != null) {
            // 데이터 조회
            List<TemperatureDocument> documents = temperatureDocumentRepository
                    .findByCreatedAtBetweenAndIsProcessedFalse(batchStartTime, batchEndTime);

            if (!documents.isEmpty()) {
                // 모든 데이터를 isProcessed = true로 변환
                List<TemperatureDocument> updatedDocuments = documents.stream()
                        .map(doc -> doc.toBuilder()
                                .isProcessed(true)
                                .build())
                        .collect(Collectors.toList());

                // 일괄 저장 (한 번에 저장)
                temperatureDocumentRepository.saveAll(updatedDocuments);

                log.info("Temperature {} 개 처리됨", documents.size());
            } else {
                log.info("처리할 Temperature 데이터 없음");
            }
        }

        return ExitStatus.COMPLETED;
    }
}