## Nacos

### 一、服务发现

服务发现组件也叫注册中心

![image.png](https://upload-images.jianshu.io/upload_images/4994935-2471b959ea64582c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



> 上图 status up 可用的服务，借鉴了SpringBoot Actuator的健康检查。
>
> 如果再微服务启动的时候，往mysql里面插入一条数据。比如说用户服务启动的时候往里面插入一条数据，user-center ip:127.0.0.1 port:8080 status: up
>
> 同理内容中心启动的时候，插入content-center。如果某个服务要停止，就可以把对应的一条数据，status修改down;
>
> 这样就实现一个简单的，服务注册发现组件；如果用户服务要找到内容中心的服务，就可以 select * from table where registry=content-center and status = up 可以筛选出正常可用的所有组件。
>
> 但是目前还有很多问题，比如说用户在每次调用前都去请求一下服务组件，那么服务组件压力要多大啊，如果服务注册组件挂了，所有的微服务就没法通信了
>
> 所有一个更加贴近生产的发现机制，是这样的。会在微服务上面做一个缓存，怎么做呢，会定时的发送上面的哪个SQL语句，而调用都去本地的缓存区调用想调用的微服务的地址。而不是先去请求一下数据库，再去调用。
>
> 这样的好处就是提高性能,不用每次调用前先去数据库查询。即使服务发现组件崩溃也不会影响服务之间的调用，因为可以调用本地缓存啊

#### 如果某个微服务挂了，怎么吧，要解决这个问题，需要修改表结构，加一个时间戳，如下图

> 每个微服务都向微服务发现组件，发送心跳。这个心跳就是一次请求，它告诉发现组件自己是活着的，如果发现组件发现某个实例，好久都没有向自己发送心跳了，它就认为这个实例挂了，把对应的status标记down；
>
> 比如：再某个时间点，微服务id=3的实例挂了不发心跳了。那么服务发现组件发现这个实例好久没发心跳了，就把它标记down。又过了一会，内容服务更新了自己本地缓存，就能感知到用户中心地址发生变化了，这个时候它就不去调用，挂了这个的用户中心实例了


![image.png](https://upload-images.jianshu.io/upload_images/4994935-db91c1e2a38b7dd8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 大白话： 你的手机通讯录里面有很多张山的手机号备注，因为他经常换手机号。当你打他电话的时候，张山1这个备注打不通，就把它删除了

#### 服务注册

实例将自身服务信息注册到注册中心,这部分信息包括服务的主机IP和服务的Port,以及暴漏服务自身的状态和访问协议信息等

#### 服务发现

实例请求注册中心所依赖的服务信息,服务实例通过注册中心,获取到注册中心的服务实例的信息,通过这些信息去请求他们提供的服务

### 二、 什么是Nocas

#### 2.1 简单来说Nacos是一个服务发现组件和一个配置服务器

![image.png](https://upload-images.jianshu.io/upload_images/4994935-4a066cc6f997675b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1. 解决了服务A怎么找到服务B;
2. 管理微服务的配置

#### 2.2 下载安装

##### 2.2.1 下载

[点击下载](https://link.juejin.cn?target=https%3A%2F%2Fgithub.com%2Falibaba%2Fnacos%2Freleases),下载后解压

##### 2.2.2 启动

```shell
startup.cmd -m standalone
```

**单机环境必须带-m standalone参数启动，否则无法启动，不带参数启动的是集群环境。**

**持久化mysql**
```shell

Nacos 的配置与安装 
Nacos 单机版本
文件路径 /Users/brzha12/brzha_imooc/micro_service_solution/soft 
下载地址 https://github.com/alibaba/nacos/releases
访问路径：http://127.0.0.1:8848/nacos/index.html 
默认的账号密码都是 nacos, 使用无痕模式吧
给 Nacos 配置自定义的 MySQL 持久化
Nacos 集群部署方式

# 解压
tar -xzvf nacos-server-2.0.0.tar.gz 
# 单机模式启动
./startup.sh -m standalone
### If use MySQL as datasource: 
spring.datasource.platform=mysql
### Count of DB: 
db.num=1
### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoRecon 
db.user.0=root
db.password.0=root
# 进 入 到 /Users/brzha12/brzha_imooc/micro_service_solution/soft/nacos/conf, 新 建 cluster.conf 文 件 , 并 填 充 内 容
172.16.3.41:8848
172.16.3.41:8858
172.16.3.41:8868
# 集 群 必 须 要 使 用     MySQL(可 以 是     PG 或 者 是 其 他 的 数 据 源 )作 为 持 久 化 的 方 式 , 因 为 需 要 能 够 访 问 到 同 一 个 数 据 源 
# 复 制 出 来 三 份     Nacos
➜ soft cp -p -r ./nacos nacos-8848 
➜ soft cp -p -r ./nacos nacos-8858 
➜ soft cp -p -r ./nacos nacos-8868

# 修改端口号
vim nacos-8848/conf/application.properties
vim nacos-8858/conf/application.properties
vim nacos-8868/conf/application.properties
# 启 动 三 个     nacos, 不 带 任 何 参 数 标 识 集 群 启 动
./nacos-8848/bin/startup.sh
```

##### 2.2.3 测试验证

打开浏览器输入http://localhost:8848/nacos/index.html#/login ，即可访问服务，默认账号密码都是nacos。

#### 2.3 服务注册到nacos

##### 2.3.1 添加依赖

```xml

<dependencies>
    <!-- spring cloud alibaba nacos discovery 依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    
</dependencies>
```

##### 2.3.2 配置

```yaml
spring:
  application:
    name: commerce-nacos-client
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
        server-addr: 172.16.200.249:8848
        #server-addr: 127.0.0.1:8848
        # server-addr: 127.0.0.1:8848,127.0.0.1:8849,127.0.0.1:8850 # Nacos 服务器地址
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7
```

##### 2.3.3 启动类添加注解

```java

@SpringBootApplication
@EnableDiscoveryClient
public class ShopProductServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopProductServerApp.class, args);
    }
}
```

##### 2.3.4 启动查看

![image.png](https://upload-images.jianshu.io/upload_images/4994935-f1373dcef83bcec9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 2.3.5 测试

```java

@Service
@Slf4j
public class NacosClientService {
    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /** 根据 service id 获取服务实例信息 */
    @GetMapping("/service-instance")
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        log.info("请求nacos客户端获取服务实例信息:{}", serviceId);
        return discoveryClient.getInstances(serviceId);
    }
}


```

[详细文档](https://mp.weixin.qq.com/s?__biz=MzkwNTI2Mjk1OA==&mid=2247486968&idx=1&sn=56f4bfcf7ecf4f46cbeff7af796330bb&chksm=c0fb2ff9f78ca6efc9a57ed9877071cffdb7073ee5b8935bf1ca00ad5f9545b5cc691028e738&scene=21#wechat_redirect)