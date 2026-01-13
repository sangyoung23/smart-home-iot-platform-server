package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.HumidityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HumidityDocumentRepository extends MongoRepository<HumidityDocument, String> {
    boolean existsByIsProcessedFalse();
}
