package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.TemperatureDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemperatureDocumentRepository extends MongoRepository<TemperatureDocument, String> {
    boolean existsByIsProcessedFalse();
}