package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.BatteryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatteryDocumentRepository extends MongoRepository<BatteryDocument, String> {
    boolean existsByIsProcessedFalse();
}
