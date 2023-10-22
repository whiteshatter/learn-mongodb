# Mongodb索引

### 创建索引
```javascript
// 创建索引
db.集合名.createIndex({name: 1, age: -1}, 参数)
// 常用参数：
// - background： 是否堵塞其他数据库操作，默认false
// - unique： 是否唯一索引。默认false
// - name： 索引名
// - expireAfterSeconds：TTL，索引有效期 


// 查看索引
db.集合名.getIndexes()

// 删除索引
db.集合名.dropIndexes()
db.集合名.dropIndex({索引名})
```