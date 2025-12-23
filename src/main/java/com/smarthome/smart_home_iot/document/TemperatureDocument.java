package com.smarthome.smart_home_iot.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-temperature")
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemperatureDocument {

    @Id
    private String id;

    private String deviceId;
    private Double temperature;
    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isProcessed = false;
}
