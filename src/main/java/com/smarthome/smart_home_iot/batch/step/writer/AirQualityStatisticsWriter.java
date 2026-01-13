package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.sensor.AirQualityStatistics;
import com.smarthome.smart_home_iot.repository.jpa.AirQualityStatisticsRepository;
import com.smarthome.smart_home_iot.service.batch.AirQualityBulkUpdateService;
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
public class AirQualityStatisticsWriter implements ItemWriter<AirQualityStatistics> {

    private final AirQualityStatisticsRepository statisticsRepository;
    private final AirQualityBulkUpdateService bulkUpdateService;

    @Override
    public void write(Chunk<? extends AirQualityStatistics> chunk) {

        for (AirQualityStatistics stat : chunk.getItems()) {
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
                        .get("processedAirQualityIds");

        if (ids != null && !ids.isEmpty()) {
            long updated = bulkUpdateService.markProcessedByIds(ids);
            log.info("AirQuality 원본 {}건 isProcessed=true 처리", updated);
        } else {
            log.info("AirQuality 원본 isProcessed 처리 실패");
        }
    }
}
