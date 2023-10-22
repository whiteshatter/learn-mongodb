# 聚合查询
### 一、初始化
```javascript
db.orders.insertMany([
    {name: "order1", total: 3, price: 4},
    {name: "order2", total: 3, price: 11},
    {name: "order3", total: 5, price: 435},
    {name: "order4", total: 5, price: 31},
    {name: "order5", total: 5, price: 143},
])
```

### 二、聚合
```javascript
// 以total进行分组
db.orders.aggregate([{$group: {_id: '$total'}}])
// 求和
db.orders.aggregate([{$group: {_id: '$total', total_price: {$sum: '$price'}}}])
// 最小值
db.orders.aggregate([{$group: {_id: '$total', min_price: {$min: '$price'}}}])
// 最大值
db.orders.aggregate([{$group: {_id: '$total', max_price: {$max: '$price'}}}])
// 将值插入数组
db.orders.aggregate([{$group: {_id: '$total', price_list: {$push: '$price'}}}])
// 将值插入集合
db.orders.aggregate([{$group: {_id: '$total', price_set: {$addToSet: '$price'}}}])

```