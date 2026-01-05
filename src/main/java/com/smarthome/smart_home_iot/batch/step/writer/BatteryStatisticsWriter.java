package com.smarthome.smart_home_iot.batch.step.writer;

import com.smarthome.smart_home_iot.domain.sensor.BatteryStatistics;
import com.smarthome.smart_home_iot.repository.jpa.BatteryStatisticsRepository;
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
public class BatteryStatisticsWriter implements ItemWriter<BatteryStatistics> {

    private final BatteryStatisticsRepository statisticsRepository;
    private final BatteryBulkUpdateService bulkUpdateService;

    @Override
    public void write(Chunk<? extends BatteryStatistics> chunk) {
        // 1️⃣ 통계 저장
        for (BatteryStatistics stat : chunk.getItems()) {
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
                        .get("processedBatteryIds");

        if (ids != null && !ids.isEmpty()) {
            long updated = bulkUpdateService.markProcessedByIds(ids);
            log.info("Battery 원본 {}건 isProcessed=true 처리", updated);
        } else {
            log.info("Battery 원본 isProcessed 처리 실패");
        }
    }
}