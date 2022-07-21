## springboot整合 secKill

### 第一步：spring-boot-seckill-simple

~~~xml

<dependencies>
    <!--redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!-- mysql数据库 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>

    <!-- SQL 分析打印 -->
    <dependency>
        <groupId>p6spy</groupId>
        <artifactId>p6spy</artifactId>
        <version>3.9.1</version>
    </dependency>

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
  datasource:
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql://localhost:3306/babybtun?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: root123456

  redis:
    host: 127.0.0.1
    port: 6379

  rabbitmq:
    host: 172.16.200.220
    port: 5672
    username: admin
    password: 123456

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/*.xml

logging:
  level:
    com.example: debug

~~~

### 第三步： 配置类

#### 1. 常量配置

```java
public interface SecKillConstant {
    public static final String SECKILL_COUNT = "seckill:count:";
    public static final String USER_COUNT = "seckill:users:";
}

public interface RabbitConstant {
    public final static String QUEUE_ORDER = "queue_order";
    //中奖队列名称
    public final static String QUEUE_HIT = "prize_queue_hit";
    //参与活动队列
    public static final String QUEUE_PLAY = "prize_queue_play";
    //中奖路由名称
    public final static String EXCHANGE_DIRECT = "prize_exchange_direct";

}

```

#### 2. 初始化配置

```java

@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public Queue review() {
        return new Queue(RabbitConstant.QUEUE_ORDER);
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
@Slf4j
public class OrderConsumer {
    @RabbitListener(queues = RabbitConstant.QUEUE_ORDER)
    public void getMessage(@Payload Map<String, String> data, @Headers Map<String, Object> headers,
                           Object msg, Channel channel, Message message) throws IOException {
        System.out.println("消费时间：" + LocalDateTime.now());
        System.out.println("消费者接收到的消息：" + JSONObject.toJSONString(data, true));
        //  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

~~~

### 第五步：业务实现

#### 1. 定时任务扫码活动

```java

@Component
@Slf4j
public class SecKillTask {
    @Autowired
    private PromotionSecKillService seckillService;
    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void startSecKill() {
        log.info("【秒杀活动】启动任务, date: {}", DateUtil.now());

        // 获取未开始的活动
        List<TPromotionSeckill> unstartSeckills = getUnstartSeckills();

        for (TPromotionSeckill ps : unstartSeckills) {
            log.info("【秒杀活动】启动, id:{}", ps.getPsId());
            //删掉以前重复的活动任务缓存
            redisTemplate.delete(SecKillConstant.SECKILL_COUNT + ps.getPsId());
            //有几个库存商品，则初始化几个list对象
            for (int i = 0; i < ps.getPsCount(); i++) {
                redisTemplate.opsForList().rightPush(SecKillConstant.SECKILL_COUNT + ps.getPsId(), ps.getGoodsId());
            }
            ps.setStatus(1);
            seckillService.updateById(ps);
        }

    }


    @Scheduled(cron = "0/15 * * * * ?")
    public void endSecKill() {
        log.info("【秒杀活动】已结束任务, date:{}", DateUtil.now());
        List<TPromotionSeckill> expireTimeList = getExpireSecKills();

        for (TPromotionSeckill ps : expireTimeList) {
            log.info("【秒杀活动】已结束 id:{}", ps.getPsId());
            ps.setStatus(2);
            seckillService.updateById(ps);
            redisTemplate.delete(SecKillConstant.SECKILL_COUNT + ps.getPsId());
        }
    }


    private List<TPromotionSeckill> getExpireSecKills() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.le(TPromotionSeckill::getEndTime, DateUtil.now());
        lambda.eq(TPromotionSeckill::getStatus, 1);
        List<TPromotionSeckill> unstartSeckills = seckillService.list(lambda);
        return unstartSeckills;
    }

    private List<TPromotionSeckill> getUnstartSeckills() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.le(TPromotionSeckill::getStartTime, DateUtil.now());
        lambda.ge(TPromotionSeckill::getEndTime, DateUtil.now());
        lambda.eq(TPromotionSeckill::getStatus, 0);
        List<TPromotionSeckill> unstartSeckills = seckillService.list(lambda);
        return unstartSeckills;
    }

}

```

#### 2. 秒杀接口定义

```java

public interface PromotionSecKillService extends IService<TPromotionSeckill> {

    public abstract String processSecKill(Long psId, String userId, Integer number);

    /**
     * 发送MQ
     * @param userId
     * @param orderNo
     * @return
     */
    public String sendOrderToQueue(String userId, String orderNo);
}


```

#### 2. 秒杀接口实现

```java
@Service
@Slf4j
public class TPromotionSeckillServiceImpl extends ServiceImpl<TPromotionSeckillMapper, TPromotionSeckill> implements PromotionSecKillService {

    @Autowired
    private TPromotionSeckillMapper seckillMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public String processSecKill(Long psId, String userId, Integer number) {
        final TPromotionSeckill ps = seckillMapper.selectById((Serializable) psId);

        checkStatus(psId, ps);

        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop(SecKillConstant.SECKILL_COUNT + ps.getPsId());
        if (ObjectUtil.isNull(goodsId)) {
            throw new SecKillException("抱歉，该商品已被抢光，下次再来吧！！");
        }

        // 判断是否抢购
        boolean isExisted = redisTemplate.opsForSet().isMember(SecKillConstant.USER_COUNT + ps.getPsId(), userId);
        if (isExisted) {
            // 退回商品
            redisTemplate.opsForList().rightPush(SecKillConstant.SECKILL_COUNT + ps.getPsId(), ps.getGoodsId());
            log.error("【秒杀活动】 您已经参加过此活动,id: {}; userId:{}", psId, userId);
            throw new SecKillException("抱歉，您已经参加过此活动，请勿重复抢购！");
        }

        String orderNo = IdUtil.getSnowflakeNextIdStr();
        log.info("【秒杀活动】 恭喜您，抢到商品啦。快去下单吧,id: {}; userId:{}; orderNo:{}", psId, userId, orderNo);
        redisTemplate.opsForSet().add(SecKillConstant.USER_COUNT + ps.getPsId(), userId);


        return orderNo;
    }

    public String sendOrderToQueue(String userId, String orderNo) {
        log.info("【秒杀活动】 准备向队列发送信息, userId:{}; orderNo:{}", userId, orderNo);
        System.out.println("准备向队列发送信息");
        //订单基本信息
        Map<String, String> data = new HashMap<String, String>();
        data.put("userid", userId);
        data.put("orderNo", orderNo);
        //附加额外的订单信息
        rabbitTemplate.convertAndSend(RabbitConstant.QUEUE_ORDER, data);
        return orderNo;
    }

    private void checkStatus(Long psId, TPromotionSeckill seckill) {
        if (ObjectUtil.isNull(seckill)) {
            log.error("【秒杀活动】 不存在,id: {}", psId);
            throw new SecKillException("活动不存在!");
        }
        if (seckill.getStatus() == 0) {
            log.error("【秒杀活动】 未开始,id: {}", psId);
            throw new SecKillException("秒杀活动未开始!");
        }
        if (seckill.getStatus() == 2) {
            log.error("【秒杀活动】 已结束,id: {}", psId);
            throw new SecKillException("秒杀活动已结束!");
        }
    }
}

```

### 第六步 测试
```java

@RestController
@RequestMapping("/")
public class SecKillController {

    @Autowired
    private PromotionSecKillService service;

    @PostMapping("/seckill")
    public Dict processSecKill(Long psId, String userId) {
        String orderNo = service.processSecKill(psId, userId, 1);
        service.sendOrderToQueue(userId, orderNo);


        return Dict.create().set("code", 0).set("message", " 恭喜您，抢到商品啦");
    }
}


```