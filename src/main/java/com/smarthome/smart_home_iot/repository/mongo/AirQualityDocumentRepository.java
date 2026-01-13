package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.AirQualityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirQualityDocumentRepository extends MongoRepository<AirQualityDocument, String> {
    boolean existsByIsProcessedFalse();
}
