package com.mongo.operation;

import com.mint.learn.mongo.MongoApplication;
import com.mongo.entity.Product;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;

@SpringBootTest(classes = MongoApplication.class)
public class UpdateOperationTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testUpdate() {
        Update update = new Update();
        update.set("count", 4);
        mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(2)), update, Product.class);
        // 没有符合条件数据就插入
        UpdateResult result = mongoTemplate.upsert(Query.query(Criteria.where("id").is(2)), update, Product.class);
        System.out.println(result.getMatchedCount());
        System.out.println(result.getUpsertedId());
        System.out.println(result.getModifiedCount());
    }
}
