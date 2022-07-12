## springboot整合 rabbitmq

### 第一步：spring-boot-mq-rabbitmq

~~~xml

<dependencies>
    <!-- mq消息队列 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
</dependencies>
~~~

### 第二步：创建application.yml文件

~~~yaml
spring:
  rabbitmq:
    host: 172.16.200.220
    port: 5672
    username: admin
    password: 123456
~~~

### 第三步： 配置类

#### 1. 常量配置

```java
public interface RabbitConsts {
    //中奖队列名称
    public final static String QUEUE_HIT = "prize_queue_hit";
    //参与活动队列
    public static final String QUEUE_PLAY = "prize_queue_play";
    //中奖路由名称
    public final static String EXCHANGE_DIRECT = "prize_exchange_direct";

    /**
     * 延迟队列
     */
    String DELAY_QUEUE = "delay.queue";

    /**
     * 延迟队列交换器
     */
    String DELAY_MODE_QUEUE = "delay.mode";
}


```

#### 2. 初始化配置

```java
package com.example.config;

/**
 * rabbit配置
 */
@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Bean
    public Queue getQueueHit() {
        return new Queue(RabbitConsts.QUEUE_HIT);
    }

    @Bean
    public Queue getQueuePlay() {
        return new Queue(RabbitConsts.QUEUE_PLAY);
    }

    /**
     * exchange
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitConsts.EXCHANGE_DIRECT);
    }

    // 绑定队列于路由
    @Bean
    Binding bindingExchangeDirect() {
        return BindingBuilder.bind(getQueueHit()).to(directExchange()).with(RabbitConsts.QUEUE_HIT);
    }

    @Bean
    Binding bindingExchangeDirect2() {
        return BindingBuilder.bind(getQueuePlay()).to(directExchange()).with(RabbitConsts.QUEUE_PLAY);
    }


    ///////////死信队列//////////////

    /**
     * 普通队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }


    /**
     * 主题模式队列
     * <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email</li>
     * <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了</li>
     * <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配</li>
     */
    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * 将指定了消息过期时间的队列绑定到交换机
     */
    @Bean
    public Binding orderCreateBinding() {
        /*
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",  // 路由key一般为事件名
                null);
    }

    @Bean
    public Binding orderReleaseBinding() {

        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
     /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        //当消息变为死信后重新发送到指定死信交换机
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        //当死信到达死信交换机后，根据该路由键投递到指定的死信队列
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        // 消息过期时间
        arguments.put("x-message-ttl", 6000);

        return new Queue("order.delay.queue", true, false, false, arguments);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setUseDirectReplyToContainer(false);
        //发送之前加一个拦截器，每次发送都会调用这个方法，方法名称已经说明了一切了
        template.setBeforePublishPostProcessors(new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //拦截逻辑添加环境变量 DynamicSourceTtl.get()
                message.getMessageProperties().getHeaders().put("dataSource", "master");
                return message;
            }
        });
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback，如果这里配置了，那么写生产者的时候不能再写confirm-callback和return-callback
        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        /**
         * 如果消息没有到exchange,则confirm回调,ack=false
         * 如果消息到达exchange,则confirm回调,ack=true
         * exchange到queue成功,则不回调return
         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                } else {
                    log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                }
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        return rabbitTemplate;
    }


}
```

### 第四步： 监听

~~~java

@Component
public class Consumer {

    @RabbitListener(queues = "order.release.order.queue")
    public void getMessage(Object msg, Channel channel, Message message) throws IOException {
        System.out.println("消费时间：" + LocalDateTime.now());
        System.out.println("消费者接收到的消息：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

~~~

### 第五步：创建消息体

~~~java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageStruct implements Serializable {
    private static final long serialVersionUID = 392365881428311040L;

    private String message;
}

~~~

### 第六步：创建测试

```java

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        // rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT, RabbitKeys.QUEUE_HIT, json);
        rabbitTemplate.convertAndSend(RabbitConsts.QUEUE_PLAY, json);

    }

    @Test
    public void ttlMsg() {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        //给MQ发送消息
        rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", json);
    }
}

```

[参考文档](https://juejin.cn/post/6998325022112612388)
[参考文档](https://blog.csdn.net/suchahaerkang/article/details/109131090?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-1-109131090-blog-120004761.pc_relevant_antiscanv2&spm=1001.2101.3001.4242.2&utm_relevant_index=4)





