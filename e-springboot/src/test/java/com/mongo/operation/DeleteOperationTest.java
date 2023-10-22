package com.mongo.operation;

import com.mint.learn.mongo.MongoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;

@SpringBootTest(classes = MongoApplication.class)
public class DeleteOperationTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testDelete() {
        mongoTemplate.remove(Query.query(Criteria.where("count").is(2)));
    }
}
