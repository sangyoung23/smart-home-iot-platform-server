package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.AirQualityStatistics;
import com.smarthome.smart_home_iot.domain.BatteryStatistics;
import com.smarthome.smart_home_iot.domain.TemperatureStatistics;
import com.smarthome.smart_home_iot.repository.jpa.AirQualityStatisticsRepository;
import com.smarthome.smart_home_iot.repository.jpa.BatteryStatisticsRepository;
import com.smarthome.smart_home_iot.service.batch.AirQualityBulkUpdateService;
import com.smarthome.smart_home_iot.service.batch.BatteryBulkUpdateService;
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
        // 1️⃣ 통계 저장
        for (AirQualityStatistics stat : chunk.getItems()) {
            statisticsRepository.findByDeviceIdAndStatDateAndStatHour(
                    stat.getDeviceId(),
                    stat.getStatDate(),
                    stat.getStatHour()
            ).ifPresentOrElse(
                    // 같은 시간대 집계 데이터가 있으면 update
                    existing -> {
                        existing.merge(stat);
                        statisticsRepository.save(existing);
                    },
                    // 같은 시간대 집계 데이터가 없으면 insert
                    () -> statisticsRepository.save(stat)
            );
        }

        // 2️⃣ 원본 데이터 처리 완료
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
