package com.smarthome.smart_home_iot.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor-power")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PowerDocument {

    @Id
    private String id;
    private String deviceId;
    private Double powerUsage;
    private Double voltage;
    private Double current;
    private Double energyTotal;
    private LocalDateTime timestamp;
}
