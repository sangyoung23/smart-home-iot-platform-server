package com.smarthome.smart_home_iot.dto.dashboard;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DashboardSummaryResponse {

    /** 전체 센서 개수 현재 5개 고정 */
    private long totalSensorCount;

    /** 정상 상태 센서 개수 */
    private long normalSensorCount;

    /** 가장 최근 데이터 수집 시간 */
    private LocalDateTime lastCollectedAt;

    /** 금일 총 집계 건수 */
    private long todayAggregationCount;

    /** 센서 타입별 상세 정보 */
    private List<SensorStatusDto> sensors;
}
