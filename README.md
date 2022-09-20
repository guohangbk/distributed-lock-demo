# 分布式锁demo
<br/>


### 常见的分布式锁解决方案有三种
三种解决方案，但是每种方案里又有多重实现方式。
- 1、数据库（表唯一索引、排他锁、乐观锁）
- 2、Redis（Redisson、RedisTemplate、Lua）
- 3、Zookeeper（唯一节点、有序临时节点两种）

***
### 本demo中提供了以下锁实现方式
- 数据库表唯一索引锁
- 数据库排他锁
- Redisson锁
- Redis Lua锁
- Zookeeper唯一节点锁等实现方式

***
### docker部署单节点zookeeper容器步骤
+ 创建并启动容器：docker run -d -p 2181:2181 --restart always --name=zookeeper zookeeper 
+ 进入容器并使用zk命令行客户端连接zk：docker run -it --rm --link zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper 
+ 进入容器后，查看结点数量： ls / 
+ 进入容器后，退出zk客户端：quit 
+ 停止并删除容器：docker stop zookeeper && docker rm zookeeper

***
> tips: sql文件中的sql是postgresql，其他数据库请自行创建表