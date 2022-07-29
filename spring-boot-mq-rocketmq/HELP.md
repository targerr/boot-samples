## SpringBoot整合rocketmq

### 一. 添加依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.apache.rocketmq</groupId>
        <artifactId>rocketmq-client</artifactId>
        <version>4.7.1</version>
    </dependency>

    <dependency>
        <groupId>org.apache.rocketmq</groupId>
        <artifactId>rocketmq-spring-boot-starter</artifactId>
        <version>2.1.1</version>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
            </exclusion>
            <exclusion>
                <groupId>org.springframework</groupId>
                <artifactId>spring-core</artifactId>
            </exclusion>
            <exclusion>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

### 二. 配置

```yaml
rocketmq:
  producer:
    group: springBootGroup
  name-server: 192.168.40.128:9876
server:
  port: 8081
```

### 三. 生产者

```java
@Component
public class Producer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    // 消息发送方法
    public void sendMessage(String topic, String msg){
        this.rocketMQTemplate.convertAndSend(topic, msg);
    }

}
```

### 四. 监听

```java
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "TestTopic")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message)  {
        System.out.println("Recived message: " + message);
    }
}

```


[参考文档](https://juejin.cn/post/7001513234851168264)