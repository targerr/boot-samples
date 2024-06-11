### RabbitMQ延时消息
**[参考](https://zhuanlan.zhihu.com/p/641458427)**

**[高级特性](https://blog.csdn.net/QRLYLETITBE/article/details/126685420)**

#### 配置类

```java

@Component
public class DeadLetterMQConfig {
    /**
     * 订单交换机
     */
    public static final String orderExchange = "mayikt_order_exchange";

    /**
     * 订单队列
     */
    public static final String orderQueue = "mayikt_order_queue";

    /**
     * 订单路由key
     */
    public static final String orderRoutingKey = "mayikt.order";
    /**
     * 死信交换机
     */
    private static final String dlxExchange = "mayikt_dlx_exchange";

    /**
     * 死信队列
     */
    private static final String dlxQueue = "mayikt_order_dlx_queue";
    /**
     * 死信路由
     */
    private static final String dlxRoutingKey = "dlx";

    /**
     * 声明死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * 声明死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueue);
    }

    /**
     * 声明订单业务交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(orderExchange);
    }

    /*
           Queue(String name,  队列名字
           boolean durable,  是否持久化
           boolean exclusive,  是否排他
           boolean autoDelete, 是否自动删除
           Map<String, Object> arguments) 属性
        */
    @Bean
    public Queue orderQueue() {
        // 订单队列绑定我们的死信交换机
        Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("x-dead-letter-exchange", dlxExchange);
        arguments.put("x-dead-letter-routing-key", dlxRoutingKey);
        return new Queue(orderQueue, true, false, false, arguments);
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }


    /**
     * 绑定订单队列到订单交换机
     *
     * @return Binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(orderRoutingKey);
    }
}
```

#### 生产者
```java
@RestController
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public  String orderQueue = "mayikt_order_queue";

    /**
     * 订单路由key
     */
    public  String orderRoutingKey = "mayikt.order";

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        String msg = "测试死信队列";
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, message -> {
            // 设置消息过期时间 10秒过期
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
        return "success";
    }
}

```

#### 消费者

```java

@Slf4j
@Component
public class OrderDlxConsumer {

    /**
     * 死信队列监听队列回调的方法
     *
     * @param msg
     */
    @RabbitListener(queues = "mayikt_order_dlx_queue")
    public void orderConsumer(String msg) {
        log.info(">死信队列消费订单消息:msg{}<<", msg);
    }
}
```


