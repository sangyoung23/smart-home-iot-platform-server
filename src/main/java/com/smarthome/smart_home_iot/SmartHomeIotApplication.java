package com.smarthome.smart_home_iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SmartHomeIotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartHomeIotApplication.class, args);
	}

}
