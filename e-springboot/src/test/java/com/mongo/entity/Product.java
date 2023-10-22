package com.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("product")
public class Product {
    @Id
    private String id;

    @Field("count")
    private Integer count;

    // 不参与文档序列化
    @Transient
    private String showName;
}
