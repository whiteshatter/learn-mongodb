package com.mongo.operation;

import com.mint.learn.mongo.MongoApplication;
import com.mongo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

@SpringBootTest(classes = MongoApplication.class)
public class AddOperationTest {
    @Resource
    private MongoTemplate mongoTemplate;


    @Test
    public void add() {
        Product product = new Product();
        product.setId("2");
        // product.setCount(2);
        // 主键存在时更新，无值属性会更新为null
        mongoTemplate.save(product);
        // 主键存在时报错,可以批量处理
        mongoTemplate.insert(product);
    }
}
