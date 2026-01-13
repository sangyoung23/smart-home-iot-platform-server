package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.sensor.HumidityStatistics;
import com.smarthome.smart_home_iot.repository.jpa.HumidityStatisticsRepository;
import com.smarthome.smart_home_iot.service.batch.HumidityBulkUpdateService;
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
public class HumidityStatisticsWriter implements ItemWriter<HumidityStatistics> {

    private final HumidityStatisticsRepository statisticsRepository;
    private final HumidityBulkUpdateService bulkUpdateService;

    @Override
    public void write(Chunk<? extends HumidityStatistics> chunk) {

        for (HumidityStatistics stat : chunk.getItems()) {
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
                        .get("processedHumidityIds");

        if (ids != null && !ids.isEmpty()) {
            long updated = bulkUpdateService.markProcessedByIds(ids);
            log.info("Humidity 원본 {}건 isProcessed=true 처리", updated);
        } else {
            log.info("Humidity 원본 isProcessed 처리 실패");
        }
    }
}
