package com.smarthome.smart_home_iot.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-air-quality")
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AirQualityDocument {

    @Id
    private String id;

    private String deviceId;
    private Double pm10;
    private Double pm25;
    private int co2;
    private int voc;
    private int light;
    private boolean gasLeak;
    private int smokeLevel;
    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isProcessed = false;
}
