package com.smarthome.smart_home_iot.repository.mongo;

import com.smarthome.smart_home_iot.document.HumidityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HumidityDocumentRepository extends MongoRepository<HumidityDocument, String> {
    // isProcessed가 false인 데이터가 있는지 확인(집계 처리 할 데이터가 있는지 확인)
    boolean existsByIsProcessedFalse();

    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 }, 'isProcessed': false }")
    List<HumidityDocument> findByCreatedAtBetweenAndIsProcessedFalse(LocalDateTime startTime, LocalDateTime endTime);
}
