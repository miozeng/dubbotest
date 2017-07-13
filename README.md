# dubbo
### 简介
Dubbo是一个分布式服务框架,解决了上面的所面对的问题，Dubbo的架构如图所示：
图片一

节点角色说明：
Provider: 暴露服务的服务提供方。
Consumer: 调用远程服务的服务消费方。
Registry: 服务注册与发现的注册中心。
Monitor: 统计服务的调用次调和调用时间的监控中心。
Container: 服务运行容器。

调用关系说明：
0. 服务容器负责启动，加载，运行服务提供者。
1. 服务提供者在启动时，向注册中心注册自己提供的服务。
2. 服务消费者在启动时，向注册中心订阅自己所需的服务。
3. 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
4. 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
5. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。


 Dubbo提供了很多协议，Dubbo协议、RMI协议、Hessian协议，我们查看Dubbo源代码，有各种协议的实现，
### zookeeper 安装
 略
 
### dubbo治理平台
1.下载
dubbo-admin-2.4.1.war
2.安装
war放在tomcat的webapps/ROOT下，然后进行解压，到webapps/ROOT/WEB-INF下，有一个dubbo.properties文件，里面指向Zookeeper ，使用的是Zookeeper 的注册中心
3.启动
然后启动tomcat服务，用户名和密码：root,并访问服务，显示登陆页面，说明dubbo-admin部署成功

### dubbo简单实例
请查看github demo

### dubbo实例2
----dubbotest 
------dubbo-common 公共依赖模块
------dubbo-user user业务模块
--------dubbo-user-api user的api模块，定义实体和借口
--------dubbo-user-core user的服务提供者，具体的业务在这边实现
--------dubbo-user-web  user的服务调用者，web模块包含前端显示
这个案例我只是简单的写了一下并没有测试通过


### 基本原理机制
Dubbo缺省协议采用单一长连接和NIO异步通讯，  
适合于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况   
分析源代码，基本原理如下：   
1.client一个线程调用远程接口，生成一个唯一的ID（比如一段随机字符串，UUID等），Dubbo是使用AtomicLong从0开始累计数字的		

2.将打包的方法调用信息（如调用的接口名称，方法名称，参数值列表等），和处理结果的回调对象callback，全部封装在一起，组成一个对象object  
3.向专门存放调用信息的全局ConcurrentHashMap里面put(ID, object)   
4.将ID和打包的方法调用信息封装成一对象connRequest，使用IoSession.write(connRequest)异步发送出去  
5.当前线程再使用callback的get()方法试图获取远程返回的结果，在get()内部，则使用synchronized获取回调对象callback的锁， 再先检测是否已经获取到结果，如果没有，然后调用callback的wait()方法，释放callback上的锁，让当前线程处于等待状态。		
6.服务端接收到请求并处理后，将结果（此结果中包含了前面的ID，即回传）发送给客户端，客户端socket连接上专门监听消息的线程收到消息，分析结果，取到ID，再从前面的ConcurrentHashMap里面get(ID)，从而找到callback，将方法调用结果设置到callback对象里。		
7.监听线程接着使用synchronized获取回调对象callback的锁（因为前面调用过wait()，那个线程已释放callback的锁了），再notifyAll()，唤醒前面处于等待状态的线程继续执行（callback的get()方法继续执行就能拿到调用结果了），至此，整个过程结束。    
当前线程怎么让它“暂停”，等结果回来后，再向后执行？   
     答：先生成一个对象obj，在一个全局map里put(ID,obj)存放起来，再用synchronized获取obj锁，再调用obj.wait()让当前线程处于等待状态，然后另一消息监听线程等到服 务端结果来了后，再map.get(ID)找到obj，再用synchronized获取obj锁，再调用obj.notifyAll()唤醒前面处于等待状态的线程。    
正如前面所说，Socket通信是一个全双工的方式，如果有多个线程同时进行远程方法调用，这时建立在client server之间的socket连接上会有很多双方发送的消息传递，前后顺序也可能是乱七八糟的，server处理完结果后，将结果消息发送    给client，client收到很多消息，怎么知道哪个消息结果是原先哪个线程调用的？   
     答：使用一个ID，让其唯一，然后传递给服务端，再服务端又回传回来，这样就知道结果是原先哪个线程的了。





