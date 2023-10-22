# Mongodb操作
## 《视频》
### 一、库操作
```javascript
// 查看所有库
show databases
show dbs

// 创建库
use learn

// 删除库
db.dropDatabase()
```


### 二、集合操作
```javascript
// 查看集合
show collections

// 创建集合
db.createCollection('users')
db.createCollection('orders')

// 删除集合
db.集合名.drop()
```

### 三、文档操作
#### 1.插入
```javascript
// 单条插入
db.users.insertOne({name: "xiaoli", age: 10, birthday: "2010-10-1"})

// 多条插入
db.users.insertMany([
    {name: "xiaowang", age: 20, birthday: "2000-10-1"},
    {name: "xiaochen", age: 30, birthday: "1990-10-1"}
])
```
#### 2.删除
```javascript
db.集合名.remove(查询条件)
```
#### 更新
```javascript
db.集合名.updateOne(查询条件, 更新内容)
db.集合名.updateMany(查询条件, 更新内容)

// 加$set为更新指定字段，不覆盖其他字段
db.users.updateOne({name: "xiaoli"}, {$set: {age: 11}})
```
#### 4.查询
```javascript
db.集合名.find(查询条件, 返回字段)

// 等值
db.users.find({age: 11})

// 小于
db.users.find({age: {$lt: 12}})

// 大于
db.users.find({age: {$gt: 12}})

// 不等于
db.users.find({age: {$ne: 11}})

// AND
db.users.find({age: 11, name: "xiaoli"})
// 同一字段多次出现后面会覆盖前面
db.users.find({age: 11, age:{$gt: 12}})
db.users.find({$and: [{age: {$gt: 10}}, {age: {$lt: 30}}]})

// OR
db.users.find({$or: [{age: 11}, {age: 20}]})

// AND OR 结合
// where age < 30 and age > 10 and (name = "xiaoli" or name = "xiaowang")
db.users.find({$and: [
        {age: {$lt: 30}},
        {age: {$gt: 10}},
        {$or: [{name: "xiaoli"}, {name: "xiaowang"}]}
    ]})

// 数组查询
db.users.insertOne({name: "xiaolin", age: 24, hobbies:["打游戏", "乒乓球"]})
db.users.find({hobbies: "乒乓球"})
db.users.find({hobbies: {$size: 2}})

// 模糊查询 /正则表达式/
db.users.find({name: /li/})

// 排序 1：升序 2: 降序
db.users.find({}).sort({age: 1, name: -1})

// 分页
db.users.find({}).skip(x).limit(y)

// 总条数
db.users.find({}).count()

// 去重
db.users.find({}).distinct('age')

// 指定返回字段 0:不返回 1:返回
db.users.find({}, {_id: 0, age: 1})
```

## 二、《权威指南》
#### 1.插入文档

**插入多条：**

```sh
db.movies.drop()
db.movies.insertMany([{"title": "1"}, {"title": "2"}, {"title": "3"}])
db.movies.find()
[
  {
    "_id": {"$oid": "650e929fc688bd0ba7647444"},
    "title": "1"
  },
  {
    "_id": {"$oid": "650e929fc688bd0ba7647445"},
    "title": "2"
  },
  {
    "_id": {"$oid": "650e929fc688bd0ba7647446"},
    "title": "3"
  }
]
```

**MongoDB接受的最大消息大小：48MB**

**插入顺序：**

insertMany第二参数可指定为"ordered"为true/fasle，true（默认）表示顺序插入，顺序插入中间某文档出现错误后续都不会再插入

```javascript
db.movies.insertMany([{"title": "1"}, {"title": "2"}, {"title": "3"}],{"ordered": false})
```

**插入校验：**

MongoDB 会对要插入的数据进行最基本的检查，如是否有"_id"、文档大小是否小于16MB等

#### 2.删除文档

**deleteOne：**

```javascript
db.movies.deleteOne({"_id": 0})
```

deleteOne只会删除找到的第一个文档

**deleteMany:**

```javascript
db.movies.deleteMany({"_id": 0})
```

#### 3.更新文档

updateOne、updateMany、replaceOne

第一个参数是更新条件，第二个参数是更新或替换的内容

##### a.更新运算符

只有一部分需要更新可以使用更新运算符

如对某网站访问人数做递增

```javascript
db.web.insertOne({"url": "demo","pageviews": 0})
db.web.updateOne({"url": "demo"}, {"$inc": {"pageviews": 1}})
```

**"$set"**

设置一个值，不存在则创建

```javascript
db.web.updateOne({"url": "demo"}, {"$set": {"pageviews": 50,"describe": "test"}})
```

**"$inc"**

增加值

```javascript
db.web.updateOne({"url": "demo"}, {"$inc": {"pageviews": 1}})
```

**"$push"**

数组运算符，向数组插入或新增一条数据

```javascript
db.web.updateOne({"url": "demo"},{"$push":{"list":{"name":"1"}}})
db.web.updateOne({"url": "demo"},{"$push":{"list":{"name":"2"}}})
[
  {
    "_id": {"$oid": "650ea6f4c688bd0ba7647448"},
    "describe": "test",
    "list": [
      {
        "name": "1"
      },
      {
        "name": "2"
      }
    ],
    "pageviews": 50,
    "url": "demo"
  }
]
```

$push可设置参数

```javascript
db.web.updateOne({"url": "demo"},{"$push": {"list": {
"$each": ["3", "4", "5", "6"],
"$slice": -2,
"$sort": -1
}}})
```

**"$slice"**表示保留个数，负数表示从后往前

**"$sort"**表示排序，在"$slice"前执行

他们都在push后执行

**$ne**

在查询条件中可使用$ne表示不存在才操作

```javascript
db.papers.updateOne({"address": {"$ne":"shaoxing"}},{"$push":{"address":"shaoxing"}})
```

**$addToSet**

和集合类似，不存在才添加，可配合$each使用

```javascript
db.papers.updateOne({"_id": ObjectId("650fc3c7dc414966c5c0fc43")},
                    {"$addToSet":{"address":{$each":["shaoxing","xiamen"]}}})
```

**$pop**

从数组头或尾删除元素，1表示从末尾删除{"$pop":{"xxx":1}}

**$pull**

指定删除条件{"$pull":{"key":"value"}}

**定位运算符$**

```json
{
  "_id" : ObjectId("4b329a216cc613d5ee930192"),
  "content" : "...",
  "comments" : [
    {
      "comment" : "good post",
      "author" : "John",
      "votes" : 0
    },
    {
      "comment" : "i thought it was too short",
      "author" : "Claire",
      "votes" : 3
    },
    {
      "comment" : "free watches",
      "author" : "Alice",
      "votes" : -5
    },
    {
      "comment" : "vacation getaways",
      "author" : "Lynn",
      "votes" : -7
    }
  ]
}
```

```javascript
db.blog.updateOne({"comments.author" : "John"}, {"$set" : {"comments.$.author" : "Jim"}})
```

$只会替换第一个匹配到的元素

**数组过滤器arrayFilters**

```javascript
db.blog.updateOne(
  {"post" : post_id },
  { $set: { "comments.$[elem].hidden" : true } },
  {
    arrayFilters: [ { "elem.votes": { $lte: -5 } }]
  }
)
```

**upsert**

有就更新，否则插入

```javascript
> db.analytics.updateOne({"url" : "/blog"}, {"$inc" : {"pageviews" : 1}}, {"upsert" : true})
```

**$setOnInsert**

插入时才设置

```javascript
> db.users.updateOne({}, {"$setOnInsert" : {"createdAt" : new Date()}}, {"upsert" : true})
```

第二次执行不会修改createdAt的值

**updateMany**

```javascript
db.users.updateMany({"birthday" : "10/13/1978"}, {"$set" : {"gift" : "Happy Birthday!"}})
```

**findOneAndUpdate**

原子操作，获取并更新，可通过设置returnNewDocument返回修改后的值

```javascript
db.processes.findOneAndUpdate({"status" : "READY"}, 
                              {"$set" : {"status" : "RUNNING"}}, 
                              {"sort" : {"priority" : -1}, "returnNewDocument": true})
```



### 4.查询


可指定返回需要的值,1表示返回，0不返回

```javascript
db.users.find({},{"name":1, "email":1,"_id":0})
```

####  4.1 查询条件


**"$lt","$lte","$gt","$gte","$ne"等比较运算符**

```javascript
db.users.find({"age":{"$lt":30,"$gt":10}})
```

可以用来查询日期

```javascript
> start = new Date("01/01/2007")
> db.users.find({"registered" : {"$lt" : start}})
```

**"$or","$in","$nin"**

```javascript
db.users.find({"user_id" : {"$in" : [12345, "joe"]}})
```

```javascript
db.raffle.find({"$or" : [{"ticket_no" : {"$in" : [725, 542, 390]}}, {"winner" : true}]})
```

有可能就应该使用"$in"，因为查询优化器可以更高效地对其进行处理。

**null**

表示值或不存在

```javascript
{ "_id" : ObjectId("4ba0f0dfd22aa494fd523621"), "y" : null }
{ "_id" : ObjectId("4ba0f0dfd22aa494fd523622"), "y" : 1 }
{ "_id" : ObjectId("4ba0f148d22aa494fd523623"), "y" : 2 }

db.c.find({"y" : null})
{ "_id" : ObjectId("4ba0f0dfd22aa494fd523621"), "y" : null }

db.c.find({"z" : null})
{ "_id" : ObjectId("4ba0f0dfd22aa494fd523621"), "y" : null }
{ "_id" : ObjectId("4ba0f0dfd22aa494fd523622"), "y" : 1 }
{ "_id" : ObjectId("4ba0f148d22aa494fd523623"), "y" : 2 }

db.c.find({"z" : {"$eq" : null, "$exists" : true}})
```

**正则表达式 $regex**

```javascript
db.users.find({"name" : /joey?/i})
```

##### 4.2 查询数组

**"$all"**

同时拥有

```javascript
db.food.find({fruit : {$all : ["apple", "banana"]}})
```

用**key.index**匹配指定下标元素

```javascript
db.food.find({"fruit.2" : "peach"})
```

**"$size"**

不能和$lt等

**"$slice"**

```javascript
db.blog.posts.findOne(criteria, {"comments" : {"$slice" : 10}})
db.blog.posts.findOne(criteria, {"comments" : {"$slice" : -10}})
db.blog.posts.findOne(criteria, {"comments" : {"$slice" : [23, 10]}}) // 返回第24 ～ 33 个元素
```

**返回匹配的数组元素**

```javascript
> db.blog.posts.find({"comments.name" : "bob"}, {"comments.$" : 1})
{
  "_id" : ObjectId("4b2d75476cc613d5ee930164"),
    "comments" : [
      {
        "name" : "bob",
        "email" : "bob@example.com",
        "content" : "good post."
      }
    ]
}
```

**范围查找数组时只要有一个元素匹配就会返回**

```javascript
{"x" : 5}
{"x" : 15}
{"x" : 25}
{"x" : [5, 25]}

db.test.find({"x" : {"$gt" : 10, "$lt" : 20}})
{"x" : 15}
{"x" : [5, 25]}
```

对数组元素可以使用$elemMath匹配每个数组元素

```javascript
db.test.find({"x" : {"$elemMatch" : {"$gt" : 10, "$lt" : 20}}})
```

如果有索引，可以使用min和max匹配

```javascript
db.test.find({"x" : {"$gt" : 10, "$lt" : 20}}).min({"x" : 10}).max({"x" : 20})
```

##### 查询内嵌文档

```json
{
  "name" : {
    "first" : "Joe",
    "last" : "Schmoe"
  },
  "age" : 45
}
```

查询：

```javascript
db.people.find({"name.first" : "Joe", "name.last" : "Schmoe"})
```

**$elemMatch**

正确指定一组条件而无须指定每个键

```javascript
db.blog.find({"comments" : {"$elemMatch" : {"author" : "joe", "score" : {"$gte" : 5}}}})
```

##### $where

可以在查询中执行任意js代码，但为安全起见，应该严格限制或消除"$where" 子句的使用

```javascript
> db.foo.insertOne({"apple" : 1, "banana" : 6, "peach" : 3})
> db.foo.insertOne({"apple" : 8, "spinach" : 4, "watermelon" : 4})
```

返回具有相同两个值的元素

```javascript
db.foo.find({"$where" : function () {
  for (var current in this) {
    for (var other in this) {
      if (current != other && this[current] == this[other]) {
        return true;
      }
    }
  }
  return false;
}});
```

##### 游标

```javascript
for(i=0; i<100; i++) {
  db.collection.insertOne({x : i});
}
var cursor = db.collection.find();
```

使用游标获取值

```javascript
while (cursor.hasNext()) {
  obj = cursor.next();
  // 执行任务
}
```

调用find 时，shell 并不会立即查询数据库，而是等到真正开始请求结果时才发送查询

**Limit、skip、sort**

可以方便实现分页

```javascript
db.stock.find({"desc" : "mp3"}).limit(50).skip(50).sort({"price" : -1})
```

应尽量减少skip的使用，跳过大量数据会非常慢

如可以使用限定条件代替skip

```javascript
var page1 = db.foo.find().sort({"date" : -1}).limit(100)
var latest = null;
// 显示第1页
while (page1.hasNext()) {
  latest = page1.next();
  display(latest);
}
// 下一页
var page2 = db.foo.find({"date" : {"$lt" : latest.date}});
page2.sort({"date" : -1}).limit(100);
```

**游标生命周期**

1. 游标遍历完结果之后

2. 客户端发送一条消息要求终止
 10 分钟没有被使用

