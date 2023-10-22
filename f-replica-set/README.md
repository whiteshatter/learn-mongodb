# 副本集
> 具有自动故障恢复功能的主从集群，主节点读写，从节点读，没有固定主节点，当主节点发生故障时整个集群会重新选取主节点

### 副本集搭建
```shell
mkdir -p repl/data1
mkdir -p repl/data2
mkdir -p repl/data3

mongod --port 27017 --dbpath data1 --bind_ip 0.0.0.0 --replSet myreplace/[localhost:27018,localhost:27019]
mongod --port 27018 --dbpath data2 --bind_ip 0.0.0.0 --replSet myreplace/[localhost:27017,localhost:27019]
mongod --port 27019 --dbpath data3 --bind_ip 0.0.0.0 --replSet myreplace/[localhost:27018,localhost:27017]
```

```javascript
// 初始化副本集
var config = {
    _id: "myreplace",
    members: [
        {_id:0, host: "xx.xx.xx.xx:27017"},
        {_id:1, host: "xx.xx.xx.xx:27018"},
        {_id:2, host: "xx.xx.xx.xx:27019"},
    ]
}
rs.initiate(config)
rs.slaveOk()
```

