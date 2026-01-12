package com.smarthome.smart_home_iot.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smarthome.smart_home_iot.domain.sensor.SensorType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SensorStatusDto {

    private SensorType sensorType;

    private boolean normal;                 // true: 정상, false: 비정상

    private String statusMessage;           // 상태 설명 (선택사항)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDataTime;     // 마지막 집계 시간

    private Double currentValue;            // 현재 값 (평균값)
}
