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

### 第二步：创建application.yml文件（默认配置）

~~~yaml
spring:
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: admin
    password: 123456
~~~

### direct类型

#### 定义消息队列（direct）

```java

@Configuration
public class RabbitDirectConfig {
    @Bean
    public Queue queue() {
        // durable:是否持久化默认false;
        // exclusive:是否当前队列专用，默认false,连接关闭后删除队列
        // autoDelete:是否自动删除，当生产者或者消费者不在使用该队列，就删除队列
        //return new Queue("simple_queue",true,false,false)
        return new Queue("simple_queue");

    }
}
```

```java

@Configuration
public class RabbitDirectConfig {
    @Bean
    public Queue directQueue() {
        return new Queue("direct_queue");
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("direct_queue2");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("direct");
    }

    @Bean
    public Binding bindingDirect2() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("direct2");
    }
}
```

#### 生产者

```java

@RestController
public class ProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/sendMsg")
    public String sendFanoutMsg(String msg) {
        /**
         * 1.队列名称
         * 2.路由key名称
         * 3.发送内容
         */
        System.out.println("使用Rabbitmq将待发送短信的订单纳入处理队列，msg：" + msg);
        amqpTemplate.convertAndSend("directExchange", "direct", msg);
        return "success";
    }

}
```

#### 监听队列

- 使用消息监听器对消息队列监听(direct)

```java

@Component
public class RabbitMessageListener {
    @RabbitListener(queues = "direct_queue")
    public void receive(String id) {
        System.out.println("已完成短信发送业务，id：" + id);
    }
}
```

- 使用多消息监听器对消息队列监听进行消息轮循处理(direct)

```java

@Component
public class RabbitMessageListener2 {
    @RabbitListener(queues = "direct_queue")
    public void receive(String id) {
        System.out.println("已完成短信发送业务（two），id：" + id);
    }
}
```

### topic类型

#### 定义消息队列(topic)

```java

@Configuration
public class RabbitTopicConfig {
    @Bean
    public Queue topicQueue() {
        return new Queue("topic_queue");
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topic_queue2");
    }

    @Bean
    public TopicExchange topicExchange() {
        return
                new TopicExchange("topicExchange");
    }

    @Bean
    public Binding bindingTopic() {
        return BindingBuilder.bind(topicQueue()).to(
                topicExchange()).with("topic.*.*");
    }

    @Bean
    public Binding bindingTopic2() {
        return BindingBuilder.bind(topicQueue2())
                .to(topicExchange()).with("topic.#");
    }
}
```

#### 绑定规则

![image.png](https://upload-images.jianshu.io/upload_images/4994935-53bb3e447523c8f9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 生产与消费消息(topic)

```java

@RestController
public class ProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/sendMsg")
    public String sendFanoutMsg(String msg) {
        /**
         * 1.队列名称
         * 2.路由key名称
         * 3.发送内容
         */
        System.out.println("使用Rabbitmq将待发送短信的订单纳入处理队列，msg：" + msg);
        amqpTemplate.convertAndSend("topicExchange", "topic.order.id", msg);
        return "success";
    }

}


```

#### 使用消息监听器对消息队列监听(topic)

```java

@Component
public class RabbitTopicMessageListener {
    @RabbitListener(queues = "topic_queue")
    public void receive(String id) {
        System.out.println("已完成短信发送业务，id：" + id);
    }

    @RabbitListener(queues = "topic_queue2")
    public void receive2(String id) {
    }
}
```

### fanout类型

#### 定义消息队列（fanout）

```java

@Configuration
public class RabbitDirectConfig {
    @Bean
    public Queue fanoutQueue() {
        return new Queue("fanout_queue");
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout_queue2");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

  
    @Bean
    public Binding bindingFanout() {
        // fanout类型无with()
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingFanout2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }
}
```

#### 生产者

```java

@RestController
public class ProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/sendMsg")
    public String sendFanoutMsg(String msg) {
        /**
         * 1.队列名称
         * 2.路由key名称
         * 3.发送内容
         */
        amqpTemplate.convertAndSend("fanoutExchange", "", msg);
        return "success";
    }

}
```

#### 监听队列

- 使用消息监听器对消息队列监听(fanout)

```java

@Component
public class RabbitMessageListener {
    @RabbitListener(queues = "fanout_queue")
    public void receive(String id) {
        System.out.println("已完成短信发送业务，id：" + id);
    }
}
```

- 使用多消息监听器对消息队列监听进行消息轮循处理(direct)

```java

@Component
public class RabbitMessageListener2 {
    @RabbitListener(queues = "fanout_queue")
    public void receive(String id) {
        System.out.println("已完成短信发送业务（two），id：" + id);
    }
}
```
