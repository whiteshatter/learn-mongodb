# 分片
> 将数据拆分，分散到不同机器，MongoDB自动分片，从应用角度分不分片没区别

![分片架构图](https://www.mongodb.com/docs/manual/images/sharded-cluster-production-architecture.bakedsvg.svg)
- Shard: 用于存储实际的数据块，实际生产环境中一个shard server角色可由几台机器组个一个replica set承担，防止主机单点故障
- Config Server:mongod实例，存储了整个 ClusterMetadata。
- Query Routers: 前端路由，客户端由此接入，且让整个集群看上去像单一数据库，前端应用可以透明使用。
- Shard Key: 片键，设置分片时需要在集合中选一个键,用该键的值作为拆分数据的依据,这个片键称之为(shard key)，片键的选取很重要,片键的选取决定了数据散列是否均匀。

### 分片搭建
```text
# 集群规划
- Shard Server 1：27017
- Shard Repl   1：27018

- Shard Server 2：27019
- Shard Repl   2：27020

- Shard Server 3：27021
- Shard Repl   3：27022

- Config Server ：27023
- Config Server ：27024
- Config Server ：27025

- Route Process ：27026
```

```shell
mkdir -p shard/{s0,s0-repl,s1,s1-repl,s2,s2-repl,config1,config2,config3}
#分片1
mongod --port 27017 --dbpath s0 --bind_ip 0.0.0.0 --shardsvr --replSet r0/[xx.xx.xx.xx:27018]
mongod --port 27018 --dbpath s0-repl --bind_ip 0.0.0.0 --shardsvr --replSet r0/[xx.xx.xx.xx:27017]
var config = {
    _id: "s0",
    members: [
        {_id:0, host: "xx.xx.xx.xx:27017"},
        {_id:1, host: "xx.xx.xx.xx:27018"},
    ]
}
rs.initiate(config)
rs.slaveOk()

#分片2
mongod --port 27019 --dbpath s1 --bind_ip 0.0.0.0 --shardsvr --replSet r1/[xx.xx.xx.xx:27020]
mongod --port 27020 --dbpath s1-repl --bind_ip 0.0.0.0 --shardsvr --replSet r1/[xx.xx.xx.xx:27019]
var config = {
    _id: "s1",
    members: [
        {_id:0, host: "xx.xx.xx.xx:27019"},
        {_id:1, host: "xx.xx.xx.xx:27020"},
    ]
}
rs.initiate(config)
rs.slaveOk()

#分片3
mongod --port 27021 --dbpath s2 --bind_ip 0.0.0.0 --shardsvr --replSet r2/[xx.xx.xx.xx:27022]
mongod --port 27022 --dbpath s2-repl --bind_ip 0.0.0.0 --shardsvr --replSet r2/[xx.xx.xx.xx:27021]
var config = {
    _id: "s2",
    members: [
        {_id:0, host: "xx.xx.xx.xx:27021"},
        {_id:1, host: "xx.xx.xx.xx:27022"},
    ]
}
rs.initiate(config)
rs.slaveOk()

#config
mongod --port 27023 --dbpath config1 --bind_ip 0.0.0.0 --replSet config/[xx.xx.xx.xx:27024,xx.xx.xx.xx:27025] --configsvr
mongod --port 27024 --dbpath config2 --bind_ip 0.0.0.0 --replSet config/[xx.xx.xx.xx:27023,xx.xx.xx.xx:27025] --configsvr
mongod --port 27025 --dbpath config3 --bind_ip 0.0.0.0 --replSet config/[xx.xx.xx.xx:27024,xx.xx.xx.xx:27023] --configsvr

var config = {
    _id: "config",
    configsvr: true,
    members: [
        {_id:0, host: "xx.xx.xx.xx:27023"},
        {_id:1, host: "xx.xx.xx.xx:27024"},
        {_id:2, host: "xx.xx.xx.xx:27025"},
    ]
}
rs.initiate(config)

#启动路由服务
mongos --port 27026 --configdb config/xx.xx.xx.xx:27023,xx.xx.xx.xx:27024,xx.xx.xx.xx:27025 --bind_ip 0.0.0.0

#登录mongos服务
1.登录 mongo --port 27026
2.use admin
3.添加分片信息
  db.runCommand({addshard:"r0/xx.xx.xx.xx:27017,xx.xx.xx.xx:27018", "allowLocal":true});
  db.runCommand({addshard:"r1/xx.xx.xx.xx:27019,xx.xx.xx.xx:27020", "allowLocal":true});
  db.runCommand({addshard:"r2/xx.xx.xx.xx:27021,xx.xx.xx.xx:27022", "allowLocal":true});
4.指定分片的数据库
  db.runCommand({enablesharding:"learn"});
5.设置库的片键信息
  db.runCommand({shardcollection: "learn.product", key: {_id: "hashed"}})
```