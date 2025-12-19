package com.smarthome.smart_home_iot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequiredArgsConstructor
public class BatchTestController {

    private final JobOperator jobOperator;

    @GetMapping("/batch/sensor/job")
    public String runSensorJob() throws Exception {

        Properties props = new Properties();
        props.put("runDate", String.valueOf(System.currentTimeMillis()));

        long executionId = jobOperator.start("sensorJob", props);

        return "Sensor batch job triggered! Execution ID: " + executionId;
    }
}
