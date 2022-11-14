### RabbitMQ 消息回调

#### 配置类

```java
@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Bean
    public Queue smsQueue() {
        return new Queue(RabbitConsts.FANOUT_SMS_QUEUE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(RabbitConsts.FANOUT_EMAIL_QUEUE);
    }


    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(RabbitConsts.EXCHANGE_SPRINGBOOT_NAME);
    }


    // 绑定交换机 sms
    @Bean
    public Binding bindingSmsFanoutExchange(Queue  smsQueue,FanoutExchange fanoutSmsExchange){
        return BindingBuilder.bind(smsQueue).to(fanoutSmsExchange);
    }

    // 绑定交换机 email
    @Bean
    public Binding bindingEmailFanoutExchange(Queue emailQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
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
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
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
                log.info("消息唯一标识: {}", correlationData);
                log.info("确认状态: {}", ack);
                log.info("造成原因: {}", cause);
                System.err.println("````````````");
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

#### 注入方式

1. 消息确认类

   ```java
   @Slf4j
   public class RabbitConfirmCallBack implements RabbitTemplate.ConfirmCallback {
       @Override
       public void confirm(CorrelationData correlationData, boolean ack, String cause) {
           log.info("消息唯一标识: {}", correlationData);
           log.info("确认状态: {}", ack);
           log.info("造成原因: {}", cause);
           System.err.println("````````````");
           if (ack) {
               log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
           } else {
               log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
           }
       }

   }
   ```
2. 消息丢失回调

```java
@Slf4j
public class RabbitReturnCallback  implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体: {}", message);
        log.info("回复编码: {}", replyCode);
        log.info("回复内容: {}", replyText);
        log.info("交换器: {}", exchange);
        log.info("路由键: {}", routingKey);
        log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
    }
}

```

3. **配置到模板中**

```java
@Configuration
@AllArgsConstructor
public class RabbitInitializingBean implements InitializingBean {

    private RabbitTemplate rabbitTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallBack());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }
}

```

[参考](https://blog.csdn.net/qq330983778/article/details/99611193)
