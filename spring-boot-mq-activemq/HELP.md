## SpringBoot整合activemq

### 一. 添加依赖

```xml

<dependencies>
    <!--ActiveMq-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-activemq</artifactId>
    </dependency>
    <!--消息队列连接池-->
    <dependency>
        <groupId>org.apache.activemq</groupId>
        <artifactId>activemq-pool</artifactId>
    </dependency>
</dependencies>
```

### 二. 配置

```yaml
server:
  port: 8081

spring:
  activemq:
    broker-url: tcp://127.0.0.1:61616
    user: admin
    password: admin
    close-timeout: 15s   # 在考虑结束之前等待的时间
    in-memory: true      # 默认代理URL是否应该在内存中。如果指定了显式代理，则忽略此值。
    non-blocking-redelivery: false  # 是否在回滚回滚消息之前停止消息传递。这意味着当启用此命令时，消息顺序不会被保留。
    send-timeout: 0     # 等待消息发送响应的时间。设置为0等待永远。
    queue-name: active.queue
    topic-name: active.topic.name.model

  #  packages:
  #    trust-all: true #不配置此项，会报错
  pool:
    enabled: true
    max-connections: 10   #连接池最大连接数
    idle-timeout: 30000   #空闲的连接过期时间，默认为30秒

  # jms:
  #   pub-sub-domain: true  #默认情况下activemq提供的是queue模式，若要使用topic模式需要配置下面配置

# 是否信任所有包
#spring.activemq.packages.trust-all=
# 要信任的特定包的逗号分隔列表（当不信任所有包时）
#spring.activemq.packages.trusted=
# 当连接请求和池满时是否阻塞。设置false会抛“JMSException异常”。
#spring.activemq.pool.block-if-full=true
# 如果池仍然满，则在抛出异常前阻塞时间。
#spring.activemq.pool.block-if-full-timeout=-1ms
# 是否在启动时创建连接。可以在启动时用于加热池。
#spring.activemq.pool.create-connection-on-startup=true
# 是否用Pooledconnectionfactory代替普通的ConnectionFactory。
#spring.activemq.pool.enabled=false
# 连接过期超时。
#spring.activemq.pool.expiry-timeout=0ms
# 连接空闲超时
#spring.activemq.pool.idle-timeout=30s
# 连接池最大连接数
#spring.activemq.pool.max-connections=1
# 每个连接的有效会话的最大数目。
#spring.activemq.pool.maximum-active-session-per-connection=500
# 当有"JMSException"时尝试重新连接
#spring.activemq.pool.reconnect-on-exception=true
# 在空闲连接清除线程之间运行的时间。当为负数时，没有空闲连接驱逐线程运行。
#spring.activemq.pool.time-between-expiration-check=-1ms
# 是否只使用一个MessageProducer
#spring.activemq.pool.use-anonymous-producers=true
```

### 三. 注解

```java
@SpringBootApplication
@EnableJms //启动消息队列
public class SpringBootMqActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqActivemqApplication.class, args);
    }
}

```

### 四. 配置类
```java
@Component
public class ActiveConfig {
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.topic-name}")
    private String password;

    @Value("${spring.activemq.queue-name}")
    private String queueName;

    @Value("${spring.activemq.topic-name}")
    private String topicName;

    @Bean(name = "queue")
    public Queue queue() {
        return new ActiveMQQueue(queueName);
    }

    @Bean(name = "topic")
    public Topic topic() {
        return new ActiveMQTopic(topicName);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(username, password, brokerUrl);
    }

    @Bean
    public JmsMessagingTemplate jmsMessageTemplate() {
        return new JmsMessagingTemplate(connectionFactory());
    }

    /**
     * 在Queue模式中，对消息的监听需要对containerFactory进行配置
     */
    @Bean("queueListener")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    /**
     * 在Topic模式中，对消息的监听需要对containerFactory进行配置
     */
    @Bean("topicListener")
    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
}

```

### 五、 生产者

```java
@RestController
public class ProducerController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    /**
     * 点对点模式
     */
    @PostMapping("/queue/test")
    public String sendQueue(@RequestBody String str) {
        this.sendMessage(queue, str);
        return "success";
    }

    /**
     * 发布/订阅模式
     */
    @PostMapping("/topic/test")
    public String sendTopic(@RequestBody String str) {
        this.sendMessage(topic, str);
        return "success";
    }

    /**
     * 发送消息，destination是发送到的队列，message是待发送的消息
     */
    private void sendMessage(Destination destination, final String message) {
        jmsMessagingTemplate.convertAndSend(destination, message);
        // 将消息封装并发送到点对点模式队列：active.test
        //jmsMessagingTemplate.convertAndSend("active.test", message);
    }
}

```

### 六、消费者

```java
@Component
public class TopicAndQueueConsumerListener {
    /**
     * queue模式的消费者
     * SendTo("active.queue.result") 会将此方法返回的数据, 写入到 active.queue.result 中去.
     */
    @SendTo("active.queue.result")
    @JmsListener(destination="${spring.activemq.queue-name}", containerFactory="queueListener")
    public String readP2PQActiveueue(String message) {
        System.out.println("queue接受到：" + message);
        return message;
    }

    /**
     * queue模式的消费者
     */
    @JmsListener(destination="active.queue.result", containerFactory="queueListener")
    public void readP2PActiveQueue1(String message) {
        System.out.println("active.queue.result接受到：" + message);
    }

    /**
     * topic模式的消费者
     */
    @JmsListener(destination="${spring.activemq.topic-name}", containerFactory="topicListener")
    public void readTopicActiveQueue(String message) {
        System.out.println("topic接受到：" + message);
    }
}
```

[参考](https://segmentfault.com/a/1190000040240126)