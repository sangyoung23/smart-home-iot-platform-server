package com.smarthome.smart_home_iot.domain.sensor;

import java.time.LocalDateTime;

public interface FullStatisticsProjection extends DailyStatisticsProjection {
    LocalDateTime getCreatedAt();
}
