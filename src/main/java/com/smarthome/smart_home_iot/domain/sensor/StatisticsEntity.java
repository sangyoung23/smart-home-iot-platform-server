package com.smarthome.smart_home_iot.domain.sensor;

import java.time.LocalDateTime;

public interface StatisticsEntity {
    LocalDateTime getCreatedAt();  // 생성 시간은 모두 동일
    Double getRepresentativeValue();  // 대표값 (각 센서마다 다른 필드 반환)
}
