package com.smarthome.smart_home_iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.smart_home_iot.domain.HumiditySensor;
import com.smarthome.smart_home_iot.dto.kafka.HumidityKafkaMessageDto;
import com.smarthome.smart_home_iot.repository.HumiditySensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final ObjectMapper objectMapper;
    private final HumiditySensorRepository humiditySensorRepository;

    @KafkaListener(topics = "humidity-topic", groupId = "sensor-group")
    public void humidityConsume(String message) {
        try {
            HumidityKafkaMessageDto humidityDto = objectMapper.readValue(message, HumidityKafkaMessageDto.class);

            HumiditySensor humidityEntity = new HumiditySensor();
            humidityEntity.setDeviceId(humidityDto.getDeviceId());
            humidityEntity.setHumidity(humidityDto.getHumidity());
            humidityEntity.setTimestamp(humidityDto.getTimestamp());

            humiditySensorRepository.save(humidityEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
