package com.smarthome.smart_home_iot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 기본 ObjectMapper는 Java Time API를 모르기 때문에 아래와 같이 등록
        // LocalDateTime, LocalDate 등 문자열을 자바 객 형태로 자동 직렬화/역직렬화 가
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
