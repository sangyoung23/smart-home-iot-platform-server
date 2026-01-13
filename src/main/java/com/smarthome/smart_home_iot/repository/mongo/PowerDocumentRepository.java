package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.PowerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PowerDocumentRepository extends MongoRepository<PowerDocument, String> {
    boolean existsByIsProcessedFalse();
}
