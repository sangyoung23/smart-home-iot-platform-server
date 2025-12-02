package com.smarthome.smart_home_iot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SensorResponseDto {
    private double temperature; // 센서가 측정한 온도 값
    private double humidity; // 센서가 측정한 습도 값
}
