package com.smarthome.smart_home_iot.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-humidity")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HumidityDocument {

    @Id
    private String id;
    private String deviceId;
    private Double humidity;
    private LocalDateTime timestamp;
}
