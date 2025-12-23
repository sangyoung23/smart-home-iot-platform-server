package com.smarthome.smart_home_iot.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-battery")
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatteryDocument {

    @Id
    private String id;

    private String deviceId;
    private int battery;
    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isProcessed = false;
}
