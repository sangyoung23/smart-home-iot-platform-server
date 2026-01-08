package com.smarthome.smart_home_iot.domain.sensor;

import java.time.LocalDate;

public interface DailyStatisticsProjection {
    LocalDate getDate();
    Double getRepresentativeValue();
}
