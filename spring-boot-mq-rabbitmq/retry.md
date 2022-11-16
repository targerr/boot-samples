### RabbitMQ重试策略

#### 配置文件

```yaml
spring:
  rabbitmq:
    host: 172.16.200.220
    port: 5672
    username: admin
    password: 123456
    listener:
      simple:
        retry:
          #开启消费者（程序出现异常的情况下会）进行重试
          enabled: true
          #最大重试次数
          max-attempts: 5
          #重试间隔次数
          initial-interval: 3000
          #消费方 开启手动ACK(坑：当序列化为JSON时，此配置会失效，见下文)
        acknowledge-mode: manual
```

#### 消费者

```java
##@Slf4j
@Component
@RabbitListener(queues = "fanout_order_queue")
public class FanoutOrderConsumer {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void process(OrderEntity orderEntity, Message message, Channel channel) throws IOException {
//        try {
        log.info(">>orderEntity:{}<<", orderEntity.toString());
        String orderId = orderEntity.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        OrderEntity dbOrderEntity = orderMapper.getOrder(orderId);
        if (dbOrderEntity != null) {
            log.info("另外消费者已经处理过该业务逻辑");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        int result = orderManager.addOrder(orderEntity);
//        int i = 1 / 0;
        log.info(">>插入数据库中数据成功<<");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            // 记录该消息日志形式  存放数据库db中、后期通过定时任务实现消息补偿、人工实现补偿
//
//            //将该消息存放到死信队列中，单独写一个死信消费者实现消费。
//        }
    }
}
```

#### [JSON序列化失效解决🎉️ ](https://www.cnblogs.com/sw008/p/11054293.html)

