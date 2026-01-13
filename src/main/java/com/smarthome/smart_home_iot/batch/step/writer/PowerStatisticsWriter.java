package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.sensor.PowerStatistics;
import com.smarthome.smart_home_iot.repository.jpa.PowerStatisticsRepository;
import com.smarthome.smart_home_iot.service.batch.PowerBulkUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PowerStatisticsWriter implements ItemWriter<PowerStatistics> {

    private final PowerStatisticsRepository statisticsRepository;
    private final PowerBulkUpdateService bulkUpdateService;

    @Override
    public void write(Chunk<? extends PowerStatistics> chunk) {

        for (PowerStatistics stat : chunk.getItems()) {
            statisticsRepository.findByDeviceIdAndStatDateAndStatHour(
                    stat.getDeviceId(),
                    stat.getStatDate(),
                    stat.getStatHour()
            ).ifPresentOrElse(
                    existing -> {
                        existing.merge(stat);
                        statisticsRepository.save(existing);
                    },
                    () -> statisticsRepository.save(stat)
            );
        }

        StepExecution stepExecution =
                StepSynchronizationManager.getContext().getStepExecution();

        List<ObjectId> ids =
                (List<ObjectId>) stepExecution
                        .getExecutionContext()
                        .get("processedPowerIds");

        if (ids != null && !ids.isEmpty()) {
            long updated = bulkUpdateService.markProcessedByIds(ids);
            log.info("Power 원본 {}건 isProcessed=true 처리", updated);
        } else {
            log.info("Power 원본 isProcessed 처리 실패");
        }
    }
}