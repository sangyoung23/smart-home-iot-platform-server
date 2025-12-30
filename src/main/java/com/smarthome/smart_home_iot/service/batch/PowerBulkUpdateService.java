package com.smarthome.smart_home_iot.service.batch;

import com.mongodb.client.result.UpdateResult;
import com.smarthome.smart_home_iot.document.PowerDocument;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerBulkUpdateService {

    private final MongoTemplate mongoTemplate;

    public long markProcessedByIds(List<ObjectId> ids) {
        Query query = new Query(Criteria.where("_id").in(ids));
        Update update = new Update().set("isProcessed", true);

        UpdateResult result = mongoTemplate.updateMulti(query, update, PowerDocument.class);

        return result.getModifiedCount();
    }
}
