# Mongodb索引
## 一、《视频》
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

## 二、《权威指南》

### 0 初始化数据

```javascript
for (i=0;i<10000;i++) {
  db.users.insertOne(
    {
      "i":i,
      "username": "user" + i,
      "age" : Math.floor(Math.random()*120),
      "created" : new Date()
    }
  )
}
```
### 1 explain

```javascript
db.users.find({"username": "user7363"}).explain("executionStats")
```

```json
[
  {
    "command": {
      "find": "users",
      "filter": {
        "username": "user7363"
      },
      "$db": "test"
    },
    "executionStats": {
      "executionSuccess": true,
      "nReturned": 1,
      "executionTimeMillis": 31,
      "totalKeysExamined": 0,
      "totalDocsExamined": 10000,
      "executionStages": {
        "stage": "filter",
        "planNodeId": 1,
        "nReturned": 1,
        "executionTimeMillisEstimate": 8,
        "opens": 1,
        "closes": 1,
        "saveState": 11,
        "restoreState": 11,
        "isEOF": 1,
        "numTested": 10000,
        "filter": "traverseF(s4, lambda(l1.0) { ((l1.0 == s7) ?: false) }, false) ",
        "inputStage": {
          "stage": "scan",
          "planNodeId": 1,
          "nReturned": 10000,
          "executionTimeMillisEstimate": 8,
          "opens": 1,
          "closes": 1,
          "saveState": 11,
          "restoreState": 11,
          "isEOF": 1,
          "numReads": 10000,
          "recordSlot": 5,
          "recordIdSlot": 6,
          "fields": ["username"],
          "outputSlots": [4]
        }
      }
    },
    "explainVersion": "2",
    "ok": 1,
    "queryPlanner": {
      "namespace": "test.users",
      "indexFilterSet": false,
      "parsedQuery": {
        "username": {
          "$eq": "user7363"
        }
      },
      "queryHash": "F8D21635",
      "planCacheKey": "333D79F9",
      "maxIndexedOrSolutionsReached": false,
      "maxIndexedAndSolutionsReached": false,
      "maxScansToExplodeReached": false,
      "winningPlan": {
        "queryPlan": {
          "stage": "COLLSCAN",
          "planNodeId": 1,
          "filter": {
            "username": {
              "$eq": "user7363"
            }
          },
          "direction": "forward"
        },
        "slotBasedPlan": {
          "slots": "$$RESULT=s5 env: { s1 = TimeZoneDatabase(Australia/Currie...America/North_Dakota/Beulah) (timeZoneDB), s3 = 1695560146605 (NOW), s2 = Nothing (SEARCH_META), s7 = \"user7363\" }",
          "stages": "[1] filter {traverseF(s4, lambda(l1.0) { ((l1.0 == s7) ?: false) }, false)} \n[1] scan s5 s6 none none none none lowPriority [s4 = username] @\"3e00f4dd-5cc0-42ba-ac73-d4587d949bcf\" true false "
        }
      },
      "rejectedPlans": []
    },
    "serverInfo": {
      "host": "lk",
      "port": 27017,
      "version": "7.0.1",
      "gitVersion": "425a0454d12f2664f9e31002bbe4a386a25345b5"
    },
    "serverParameters": {
      "internalQueryFacetBufferSizeBytes": 104857600,
      "internalQueryFacetMaxOutputDocSizeBytes": 104857600,
      "internalLookupStageIntermediateDocumentMaxSizeBytes": 104857600,
      "internalDocumentSourceGroupMaxMemoryBytes": 104857600,
      "internalQueryMaxBlockingSortMemoryUsageBytes": 104857600,
      "internalQueryProhibitBlockingMergeOnMongoS": 0,
      "internalQueryMaxAddToSetBytes": 104857600,
      "internalDocumentSourceSetWindowFieldsMaxMemoryBytes": 104857600,
      "internalQueryFrameworkControl": "trySbeEngine"
    }
  }
]
```

"totalDocsExamined": 10000表示扫描了所有项

### 2 创建索引

```javascript
db.users.createIndex({"username":1})
```

```json
[
  {
    "command": {
      "find": "users",
      "filter": {
        "username": "user7363"
      },
      "$db": "test"
    },
    "executionStats": {
      "executionSuccess": true,
      "nReturned": 1,
      "executionTimeMillis": 6,
      "totalKeysExamined": 1,
      "totalDocsExamined": 1,
      "executionStages": {
        "stage": "nlj",
        "planNodeId": 2,
        "nReturned": 1,
        "executionTimeMillisEstimate": 0,
        "opens": 1,
        "closes": 1,
        "saveState": 0,
        "restoreState": 0,
        "isEOF": 1,
        "totalDocsExamined": 1,
        "totalKeysExamined": 1,
        "collectionScans": 0,
        "collectionSeeks": 1,
        "indexScans": 0,
        "indexSeeks": 1,
        "indexesUsed": ["username_1"],
        "innerOpens": 1,
        "innerCloses": 1,
        "outerProjects": [],
        "outerCorrelated": [4, 7, 8, 9, 10],
        "outerStage": {
          "stage": "cfilter",
          "planNodeId": 1,
          "nReturned": 1,
          "executionTimeMillisEstimate": 0,
          "opens": 1,
          "closes": 1,
          "saveState": 0,
          "restoreState": 0,
          "isEOF": 1,
          "numTested": 1,
          "filter": "(exists(s5) && exists(s6)) ",
          "inputStage": {
            "stage": "ixseek",
            "planNodeId": 1,
            "nReturned": 1,
            "executionTimeMillisEstimate": 0,
            "opens": 1,
            "closes": 1,
            "saveState": 0,
            "restoreState": 0,
            "isEOF": 1,
            "indexName": "username_1",
            "keysExamined": 1,
            "seeks": 1,
            "numReads": 2,
            "indexKeySlot": 9,
            "recordIdSlot": 4,
            "snapshotIdSlot": 7,
            "indexIdentSlot": 8,
            "outputSlots": [],
            "indexKeysToInclude": "00000000000000000000000000000000",
            "seekKeyLow": "s5 ",
            "seekKeyHigh": "s6 "
          }
        },
        "innerStage": {
          "stage": "limit",
          "planNodeId": 2,
          "nReturned": 1,
          "executionTimeMillisEstimate": 0,
          "opens": 1,
          "closes": 1,
          "saveState": 0,
          "restoreState": 0,
          "isEOF": 1,
          "limit": 1,
          "inputStage": {
            "stage": "seek",
            "planNodeId": 2,
            "nReturned": 1,
            "executionTimeMillisEstimate": 0,
            "opens": 1,
            "closes": 1,
            "saveState": 0,
            "restoreState": 0,
            "isEOF": 0,
            "numReads": 1,
            "recordSlot": 11,
            "recordIdSlot": 12,
            "seekKeySlot": 4,
            "snapshotIdSlot": 7,
            "indexIdentSlot": 8,
            "indexKeySlot": 9,
            "indexKeyPatternSlot": 10,
            "fields": [],
            "outputSlots": []
          }
        }
      }
    },
    "explainVersion": "2",
    "ok": 1,
    "queryPlanner": {
      "namespace": "test.users",
      "indexFilterSet": false,
      "parsedQuery": {
        "username": {
          "$eq": "user7363"
        }
      },
      "queryHash": "F8D21635",
      "planCacheKey": "CF66124F",
      "maxIndexedOrSolutionsReached": false,
      "maxIndexedAndSolutionsReached": false,
      "maxScansToExplodeReached": false,
      "winningPlan": {
        "queryPlan": {
          "stage": "FETCH",
          "planNodeId": 2,
          "inputStage": {
            "stage": "IXSCAN",
            "planNodeId": 1,
            "keyPattern": {
              "username": 1
            },
            "indexName": "username_1",
            "isMultiKey": false,
            "multiKeyPaths": {
              "username": []
            },
            "isUnique": false,
            "isSparse": false,
            "isPartial": false,
            "indexVersion": 2,
            "direction": "forward",
            "indexBounds": {
              "username": ["[\"user7363\", \"user7363\"]"]
            }
          }
        },
        "slotBasedPlan": {
          "slots": "$$RESULT=s11 env: { s1 = TimeZoneDatabase(Australia/Currie...America/North_Dakota/Beulah) (timeZoneDB), s3 = 1695560698030 (NOW), s6 = KS(3C757365723733363300FE04), s2 = Nothing (SEARCH_META), s5 = KS(3C7573657237333633000104), s10 = {\"username\" : 1} }",
          "stages": "[2] nlj inner [] [s4, s7, s8, s9, s10] \n    left \n        [1] cfilter {(exists(s5) && exists(s6))} \n        [1] ixseek s5 s6 s9 s4 s7 s8 [] @\"3e00f4dd-5cc0-42ba-ac73-d4587d949bcf\" @\"username_1\" true \n    right \n        [2] limit 1 \n        [2] seek s4 s11 s12 s7 s8 s9 s10 [] @\"3e00f4dd-5cc0-42ba-ac73-d4587d949bcf\" true false \n"
        }
      },
      "rejectedPlans": []
    },
    "serverInfo": {
      "host": "lk",
      "port": 27017,
      "version": "7.0.1",
      "gitVersion": "425a0454d12f2664f9e31002bbe4a386a25345b5"
    },
    "serverParameters": {
      "internalQueryFacetBufferSizeBytes": 104857600,
      "internalQueryFacetMaxOutputDocSizeBytes": 104857600,
      "internalLookupStageIntermediateDocumentMaxSizeBytes": 104857600,
      "internalDocumentSourceGroupMaxMemoryBytes": 104857600,
      "internalQueryMaxBlockingSortMemoryUsageBytes": 104857600,
      "internalQueryProhibitBlockingMergeOnMongoS": 0,
      "internalQueryMaxAddToSetBytes": 104857600,
      "internalDocumentSourceSetWindowFieldsMaxMemoryBytes": 104857600,
      "internalQueryFrameworkControl": "trySbeEngine"
    }
  }
]
```

此时 "totalDocsExamined": 1

### 复合索引

```javascript
db.users.createIndex({"age":1,"username":1})
```

**等值查询排序**

```javascript
db.users.find({"age" : 21}).sort({"username" : -1})
```

利用age找到索引再遍历，比较高效

**范围查询排序**

```javascript
db.users.find({"age" : {"$gte" : 21, "$lte" : 30}}).sort({"username" : 1})
```

对所有age匹配到的值进行排序，效率一般较低（需要看排序的数量）

**可以创建另一复合索引**

```javascript
db.users.createIndex({"username":1,"age":1})
```

通过username找到值后对age进行过滤，需要全表遍历，但不用排序，匹配数据较多的情况下效率比之前高

推荐构造索引时将排序键放在首位

### Mongodb如何选择索引

假如有5个索引，有一个查询进入，5个索引中的3个被标识为该查询的候选索引。然后，MongoDB会创建3个查询计划，分别为每个索引创建1个，并在3个并行线程中运行此查询，每个线程使用不同的索引，最快返回的以后会选择它作为索引，这个竞争会经历多次（试用期），如下所示：

![Mongdb索引过程](https://xiaoli123.oss-cn-hangzhou.aliyuncs.com/notebook/mongodb/Mongdb索引过程.png)

随着时间的推移以及集合和索引的变化，查询计划可能会从缓存中被淘汰，其他导致索引计划淘汰：

1. 重建特定的索引
2. 添加或删除索引
3. 显式清除计划缓存
4. mongod 进程的重启

### 使用复合索引

数据：学生数据集：

```json
{
  "_id" : ObjectId("585d817db4743f74e2da067c"),
  "student_id" : 0,
  "scores" : [
    {
      "type" : "exam",
      "score" : 38.05000060199827
    },
    {
      "type" : "quiz",
      "score" : 79.45079445008987
    },
    {
      "type" : "homework",
      "score" : 74.50150548699534
    },
    {
      "type" : "homework",
      "score" : 74.68381684615845
    }
  ],
  "class_id" : 127
}
```

**创建索引及查询：**

```javascript
// 创建索引
db.students.createIndex({"class_id": 1})
db.students.createIndex({student_id: 1, class_id: 1})

// 查询
db.students.find({student_id:{$gt:500000}, class_id:54}).sort({student_id:1}).explain("executionStats")
```

```json
// 部分查询结果
{
  "executionStats": {
    "executionSuccess": true,
    "nReturned": 9903,
    "executionTimeMillis": 4325,
    "totalKeysExamined": 850477,
    "totalDocsExamined": 9903,
    "executionStages": {
      "stage": "FETCH",
      "nReturned": 9903,
      "executionTimeMillisEstimate": 3485,
      "works": 850478,
      "advanced": 9903,
      "needTime": 840574,
      "needYield": 0,
      "saveState": 6861,
      "restoreState": 6861,
      "isEOF": 1,
      "invalidates": 0,
      "docsExamined": 9903,
      "alreadyHasObj": 0,
      "inputStage": {
        "stage": "IXSCAN",
        "nReturned": 9903,
        "executionTimeMillisEstimate": 2834,
        "works": 850478,
        "advanced": 9903,
        "needTime": 840574,
        "needYield": 0,
        "saveState": 6861,
        "restoreState": 6861,
        "isEOF": 1,
        "invalidates": 0,
        "keyPattern": {
          "student_id": 1,
          "class_id": 1
        },
        "indexName": "student_id_1_class_id_1",
        "isMultiKey": false,
        "multiKeyPaths": {
          "student_id": [ ],
          "class_id": [ ]
        },"isUnique": false,
        "isSparse": false,
        "isPartial": false,
        "indexVersion": 2,
        "direction": "forward",
        "indexBounds": {
          "student_id": [
            "(500000.0, inf.0]"
          ],
          "class_id": [
            "[54.0, 54.0]"
          ]
        },"keysExamined": 850477,
        "seeks": 840575,
        "dupsTested": 0,
        "dupsDropped": 0,
        "seenInvalidated": 0
      }
    }
  }
}
```

totalKeysExamined为遍历的键总数，nReturned为返回数，在本例中，为了定位9903 个匹配文档，一共检查了850 477 个索引键。

**获胜索引计划**

```json
"winningPlan": {
  "stage": "FETCH",
  "inputStage": {
    "stage": "IXSCAN",
    "keyPattern": {
      "student_id": 1,
      "class_id": 1
    },
```

**失败计划**

```json
"rejectedPlans": [
  {
    "stage": "SORT",
    "sortPattern": {
      "student_id": 1
    },
```

**查询语句**

```javascript
db.students.find({student_id:{$gt:500000}, class_id:54})
	.sort({student_id:1})
	.explain("executionStats")
```

成功计划通过student_id索引直接找到排序好的数据，失败计划通过class_id查找，对找到后的数据进行排序

ps：当前情况其实通过class_id查找再排序效率较高，因为排序数据量小



重新设计索引：

```javascript
db.students.createIndex({class_id:1, student_id:1})
```

该索引等值查询可以通过class_id直接查到排好序的数据

假如要更改成用final_grade,排序：

```javascript
db.students.find({student_id:{$gt:500000}, class_id:54})
	.sort({final_grade:1})
	.explain("executionStats")
```

需要用到索引

```javascript
db.students.createIndex({class_id:1, final_grade:1, student_id:1})
```

**索引设计总结：**

- 等值过滤的键应该在最前面

- 用于排序的键应该在多值字段之前

- 多值过滤的键应该在最后面。

**注意事项：**

**a. 选择键的方向**

多值排序时需要考虑索引方向。如按照年龄排序后按名字降序，索引设计为{"age" : 1, "username" : -1}

**b. 覆盖查询**

假如需要的数据都在索引中，可以不获取实际文档，只返回指定字段，避免返回_id等非索引字段

**c. 隐式索引**

根据最左前缀原则复合索引可以充当多个不同索引，如{"age" : 1, "username" : 1}可以充当{"age" : 1}使用

###  $运算符如何使用索引

**1. 低效运算符**

"$ne"，取反较为低效，通常需要全表查询

**2. 范围**

设计基于多个字段的索引时，应该将用于精确匹配的字段（如"x" : 1）放在最前面，将用于范围匹配的字段（如"y": {"$gt" : 3, "$lt" : 5}）放在最后面。这样可以使查询先用第一个索引键进行精确匹配，然后再用第二个索引范围在这个结果集内部进行搜索。

**3.OR查询**

$or是执行两次查询然后将结果合并，不同查询会使用不同索引，通常来说，执行两次查询再将结果合并的效率不如单次查询高，因此应该尽可能使用"$in" 而不是"$or"。

### 索引对象和数组

**1. 索引内嵌文档**

用户文档可能会有一个描述每个用户位置的内嵌文档

```json
{
  "username" : "sid",
  "loc" : {
    "ip" : "1.2.3.4",
    "city" : "Springfield",
    "state" : "NY"
  }
}
```

可以在子字段创建索引

```javascript
db.users.createIndex({"loc.city" : 1})
```

**ps：**对内嵌文档本身（如"loc"）创建索引的行为与对内嵌文档的某个字段（如"loc.city"）创建索引的行为非常不同。对整个子文档创建索引只会提高针对整个子文档进行查询的速度。只有在进行与子文档字段顺序完全匹配的查询时（比如db.users.find({"loc": {"ip" : "123.456.789.000", "city" : "Shelbyville", "state" : "NY"}}})），查询优化器才能使用"loc" 上的索引。而对于db.users.find({"loc.city" : "Shelbyville"}) 这样的查询是无法使用索引的。

**2.索引数组**

如对博客文章集合中内嵌的"comments" 数组的"date" 键创建索引，对数组创建索引实际上就是对数组的每一个元素创建一个索引项，所以如果一篇文章有20条评论，那么它就会有20 个索引项。对于单次的插入、更新或删除，每一个数组项可能都需要更新

整个数组是无法作为一个实体创建索引的：对数组创建索引就是对数组中的每个元素创建索引，而不是对数组本身创建索引。

如{"x":1}对{"x":[1, 2, 5]}中每个元素做索引

索引项中只有一个字段是可以来自数组的。这是为了避免在多键索引中的索引项数量爆炸式地增长

### 索引基数

指某字段有多少个不同值，如性别可能有男、女、未知三个，email可能有很多，age介于两者之间

基数越高，索引越有用，否则可能需要接近全表扫描

#### explain输出

**重要字段：**

"executionTimeMillis"：查询的执行速度，即从服务器接收请求到发出响应的时间。然而，如果MongoDB 尝试了多个查询计划，那么"executionTimeMillis" 反映的是所有查询计划花费的总运行时间，而不是所选的最优查询计划所花费的时间。

"isMultiKey" : 本次查询是否使用了多键索引

"nReturned" : 本次查询返回的文档数量。

"totalDocsExamined": MongoDB 按照索引指针在磁盘上查找实际文档的次数，如果查询中包含的查询条件不是索引的一部分，或者请求的字段没有包含在索引中，MongoDB 就必须查找每个索引项所指向的文档。

"totalKeysExamined"：如果使用了索引，那么这个数字就是查找过的索引条目数量。如果本次查询是一次全表扫描，那么这个数字就表示检查过的文档数量。

"stage" :MongoDB 是否可以使用索引完成本次查询。如果不可以，那么会使用"COLLSCAN" 表示必须执行集合扫描来完成查询。

"needYield"：为了让写请求顺利进行，本次查询所让步（暂停）的次数

"indexBounds"：这描述了索引是如何被使用的，并给出了索引的遍历范围

### 何时不使用索引

结果集在原集合中所占的百分比越大，索引就会越低效，因为使用索引需要进行两次查找：一次是查找索引项，一次是根据索引的指针去查找其指向的文档。而全表扫描只需进行一次查找：查找文档。

### 常见索引类型

#### 唯一索引

```javascript
db.users.createIndex(
  {"firstname" : 1},
  {"unique" : true})
```

**ps:**在某些情况下，一个值可能不会被索引。索引桶（index bucket）的大小是有限制的，如果某个索引项超过了它的限制，这个索引项就不会被包含在索引中。如果一个文档的字段由于大小限制不能被索引，那么MongoDB 就不会返回任何类型的错误或警告。这意味着大小超过8KB 的键不会受到唯一索引的约束：比如，你可以插入多个相同的8KB 字符串。

**复合唯一索引**

在复合唯一索引中，单个键可以具有相同的值，但是索引项中所有键值的组合最多只能在索引中出现一次。

#### 部分索引

如果一个字段可能存在也可能不存在，但当其存在时必须是唯一的，那么可以将"unique" 选项与"partial" 选项组合在一起使用。

要创建部分索引，需要包含"partialFilterExpression" 选项，部分索引不必是唯一的。要创建非唯一的部分索引，只需去掉"unique" 选项即可。

```javascript
db.users.ensureIndex({"email" : 1}, {"unique" : true, "partialFilterExpression" : { email: { $exists: true } }})
```

**注意：**

根据是否使用部分索引，相同的查询可能返回不同的结果。假设有一个集合，其中大多数文档有"x" 字段，但有一个文档没有：

```json
[
  { "_id" : 0 },
  { "_id" : 1, "x" : 1 },
  { "_id" : 2, "x" : 2 },
  { "_id" : 3, "x" : 3 },
]
```

当在"x" 上执行查询find({"x" : {"$ne" : 2}})时，它会返回所有匹配的文档：

```json
{ "_id" : 0 }
{ "_id" : 1, "x" : 1 }
{ "_id" : 3, "x" : 3 }
```

如果在"x" 上创建一个部分索引，那么"_id" : 0 的文档将不会被包含在索引中

```json
{ "_id" : 1, "x" : 1 }
{ "_id" : 3, "x" : 3 }
```

### 索引管理

数据库索引的所有信息都存储在system.indexes 集合中，只能通过getIndexes()、createIndex、createIndexes 和dropIndexes数据库命令来对它进行操作。

#### 标识索引

每个索引都有一个可用于标识该索引的名称，默认为keyname1_dir1_keyname2_dir2_..._keynameN_dirN，keynameX 是索引的键，dirX 是索引的方向（1 或-1）

#### 修改索引

可以使用dropIndex 命令删除不再需要的索引：

```javascript
db.people.dropIndex("x_1_y_1")
```

如果可以选择，在现有文档中创建索引要比先创建索引然后插入所有文档中稍微快一些。