package com.mongo.operation;

import com.mint.learn.mongo.MongoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

@SpringBootTest(classes = MongoApplication.class)
public class MongoCollectionOperationTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void createCollection() {
        String collectionName = "product";
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }

    @Test
    public void deleteCollection(String name) {
        mongoTemplate.dropCollection(name);
    }
}
