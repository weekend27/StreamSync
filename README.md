Data-Synchronization
======================
一个简单的分布式流式数据缓冲同步框架。

#### 1. 系统组成

```
1. 整个系统是典型的Master/Slave架构，分为消息控制端以及数据同步端，其中，数据同步端有多个，消息控制端有一个。
   其中消息控制端并不处理缓冲数据，只负责保存各个Slave节点的同步信息，以及根据数据到达情况以及是否超时情况向各个Slave节点下达数据发放的命令。
   各个Slave节点负责接收上游的数据，并发送数据初次到达服务器以及数据完全到达服务器的消息给Master节点，并根据Master节点返回的消息作进一步的处理。
2. Master与Slave节点之间的通信使用了异步通信框架akka。
3. 缓冲同步后的数据最终流向了Kafka消息队列。
```

#### 2. 消息类型

```
【1】 <Default1(short)> server->control: 接到新一组第一个包
【2】 <Default1(short)> server->control: 接到新一组最后一个包
【3】 <Default1(short)> control->server: 发送数据给Kafka
【4】 <Default1(short), sleepTime(long)> control->sleep: 哪一组包应该休眠多长时间
【5】 <Default1(short)> sleep->control: 哪一组包休眠时间结束，唤醒
```

#### 3. Slave节点

```
(1) 数据结构
链表：packageList(LinkedList<Package>)
	Package Object include: <Default1(short), count(int), DataArray(JNData[])>
集合：isDeletedSet(Set<Short>)

(2) 接包流程
>>>传入参数inData(JNData)
如果isDeletedSet为空或者isDeletedSet里面不含有inData.Default1:
	如果packageList为空，则添加 inData 到 packageList，发送first(Default1)到control node：
		如果inData.Default2 == 1, 则发送last(Default1)到control node
	否则，遍历packageList，如果找到Default1 == inData.Default1的位置插入inData，count++：
		如果inData.Default2 == count, 则发送last(Default1)到control node
	如果遍历后没找到相同的Default1，则将inData插入到链表packageList末尾，发送first(Default1)到control node：
		如果inData.Default2 == 1, 则发送last(Default1)到control node

(3) 接消息流程
>>>传入参数Default1(short)
添加Default1到isDeletedSet，标记该Default1已删除
遍历packageList，找到Default1对应的Package，发送到Kafaka，然后删除
```

#### 4. Master节点

```
(1) 数据结构
链表：mlist(LinkedList<MasterTable>)
	MasterTable Object include: <Default1(short), recvTime(long), finishNum(int), startNum(int)>

(2) 接首次消息流程processMsgFirst
>>>传入参数Default1(short)
如果mlist为空，添加Default1到mlist，启动休眠流程sleep(Default1, T)
否则，遍历mlist，找到Default1
	判断是否超时(currTime - recvTime > T)，如果是，则发送save(Default1)，然后删除
	否则，更新startNum
如果遍历后没找到相同的Default1，添加Default1到mlist，启动休眠流程sleep(Default1, T)

(3) 接完成消息流程processMsgLast
>>>传入参数Default1(short)
遍历mlist，找到Default1
	判断是否超时(currTime - recvTime > T)，如果是，则发送save(Default1)，然后删除
	否则，更新finishNum
		如果finishNum == servers的数量，集齐7个即可召唤神龙，则发送save(Default1)，然后删除
		否则，只更新finishNum

(4) 接唤醒消息流程processMsgAwake
>>>传入参数Default1(short)
遍历mlist，查找Default1
	如果找到，则发送save(Default1)，然后删除
	否则，向休眠流程返回链表头部的Default1以及剩余休眠时间

(5) 休眠流程
>>>传入参数Default1(short), sleepTime(long)
休眠sleepTime后返回Default1(short)
```

#### 5. 配置文件

```
akka通信配置文件为：src/akka.conf
sync同步配置文件为：src/sync.properties
```

#### 6. Contact me?

>```Mail```: [weekend27@163.com](mailto:weekend27@163.com)

>```Blog```: [codetopia](http://www.codetopia.cn)

any bugs? send mail, and I will appreciate your help.
