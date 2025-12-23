package com.smarthome.smart_home_iot.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-power")
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PowerDocument {

    @Id
    private String id;

    private String deviceId;
    private Double powerUsage;
    private Double voltage;
    private Double current;
    private Double energyTotal;
    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isProcessed = false;
}
