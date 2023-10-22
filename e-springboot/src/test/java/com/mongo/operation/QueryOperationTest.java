package com.mongo.operation;

import com.mint.learn.mongo.MongoApplication;
import com.mongo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = MongoApplication.class)
public class QueryOperationTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testFind() {
        // 查询所有
        List<Product> productList = mongoTemplate.findAll(Product.class);
        productList.forEach(System.out::println);

        // 根据id查询
        Product product = mongoTemplate.findById("2", Product.class);
        System.out.println(product);

        // 等值查询
        Query query = Query.query(Criteria.where("count").is(1));
        // 大于小于不等于
        query.addCriteria(Criteria.where("count").lt(34));
        query.addCriteria(Criteria.where("count").gt(1));
        query.addCriteria(Criteria.where("count").ne(5));

        // 多个and or查询时new Criteria()
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("count").is(3),
                Criteria.where("count").is(4)
        );
        query.addCriteria(criteria);

        // 排序
        Query querySort = new Query();
        querySort.with(Sort.by(Sort.Order.desc("count")));

        // 分页查询
        Query querySortPage = new Query();
        querySortPage.with(Sort.by(Sort.Order.desc("count")))
                .skip(0)
                .limit(5);

        // 总条数
        long count = mongoTemplate.count(new Query(), Product.class);
        System.out.println(count);

        // 去重
        List<Integer> countList = mongoTemplate.findDistinct(new Query(), "count", Product.class, Integer.class);
        System.out.println(countList);

        // 使用原生字符串进行查询
        BasicQuery basicQuery = new BasicQuery("{count: 1}");
        mongoTemplate.find(basicQuery, Product.class);
    }
}
