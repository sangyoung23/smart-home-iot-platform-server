package com.smarthome.smart_home_iot.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-humidity")
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HumidityDocument {

    @Id
    private String id;

    private String deviceId;
    private Double humidity;
    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isProcessed = false;
}
