# Mongodb操作
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
#### 3.更新
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



