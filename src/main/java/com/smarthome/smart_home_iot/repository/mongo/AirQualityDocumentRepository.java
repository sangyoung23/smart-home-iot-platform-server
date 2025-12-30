package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.AirQualityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;

public interface AirQualityDocumentRepository extends MongoRepository<AirQualityDocument, String> {
    // isProcessed가 false인 데이터가 있는지 확인(집계 처리 할 데이터가 있는지 확인)
    boolean existsByIsProcessedFalse();
}
