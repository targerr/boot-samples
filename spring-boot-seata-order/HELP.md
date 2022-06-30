# 整合seata

## 第一步：创建starter工程spring-boot-seata-order xml文件

> 具体实现,查考代码示例

```xml

<dependencies>

    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

</dependencies>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2.2.3.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <!--feign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>Hoxton.SR8</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>


```

## 第二步： 配置文件

```yaml
server:
  port: 8085
spring:
  cloud:
    alibaba:
      seata:
        tx-service-group: seata-order
  application:
    name: seata-order

seata:
  service:
    vgroup-mapping:
      seata-order: default
    grouplist:
      default: 127.0.0.1:8091

```

## 第三步：服务类

#### 1.1 service

```java

public interface OrderService {
    public Boolean create(Integer count);
}


```

#### 1.2 restTemplate相关实现类

```java
  @Bean
public RestTemplate restTemplate(){
        return new RestTemplate();
        }
```

```java

@Slf4j
@Service
public class OrderRestServiceImpl implements OrderService {
    // RestTemplate必须注入spring管理负责分布式事务不生效
    @Autowired
    private RestTemplate restTemplate;

    @GlobalTransactional
    public Boolean create(Integer count) {
        //调用product 扣库存
        String url = "http://localhost:8086/deduct?productId=5001&count=" + count;
        Boolean result = restTemplate.getForObject(url, Boolean.class);

        if (result != null && result) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order发生异常了");
            }
            log.info("数据库开始创建订单");
            return true;
        }
        return false;
    }
}


```

#### 1.3 feign 实现类

```java

@Service
@Slf4j
public class OrderFeignServiceImpl implements OrderService {
    //Feign
    @Autowired
    private ProductFeignClient productFeignClient;

    @GlobalTransactional
    public Boolean create(Integer count) {
        //调用product 扣库存
        Boolean result = productFeignClient.deduct(5001L, count);
        if (result != null && result) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order故意发生异常!");
            }
            log.info("数据库开始创建订单");
            return true;
        }

        return false;
    }
}

```

#### 1.4 controller

```java

@RestController
public class OrderController {

    @Resource(name = "orderRestServiceImpl")
    private OrderService orderRestServiceImpl;

    @Resource(name = "orderFeignServiceImpl")
    private OrderService orderFeignServiceImpl;

    /**
     * restTemplate方式调用
     * @param count
     * @return
     */
    @GetMapping("/v1create")
    public Boolean create(@RequestParam Integer count) {
        return orderRestServiceImpl.create(count);
    }

    /**
     * feign调用
     * @param count
     * @return
     */
    @GetMapping("/v2create")
    public Boolean createV2(@RequestParam Integer count) {
        return orderFeignServiceImpl.create(count);
    }
}

```

---

## 第四步： 测试

#### 测试前

![image.png](https://upload-images.jianshu.io/upload_images/4994935-2589fa1cc5223b79.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 测试后

> http://127.0.0.1:8085/create?count=5

<span style="color: red; "> 
故意引发错看是否扣除成功
</span>

#### 报错后仍然扣除成功,有问题 引入seata解决

![image.png](https://upload-images.jianshu.io/upload_images/4994935-82f49f57a8ec47b5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
## 使用Seata组件

#### 版本控制

![image.png](https://upload-images.jianshu.io/upload_images/4994935-e8cec26da20da345.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>  spring cloud alibaba对seata又做了一步封装,使用更方便


### [seata下载](https://github.com/seata/seata/releases/tag/v1.3.0)
### [官方文档](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

![image.png](https://upload-images.jianshu.io/upload_images/4994935-19d10e6640af3e3c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 演示demo

![image.png](https://upload-images.jianshu.io/upload_images/4994935-58f91b55f9f002e4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 配置文件

![image.png](https://upload-images.jianshu.io/upload_images/4994935-81aa0399f6dc9d1a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### [Demo地址](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/seata-example/order-service/src/main/resources/application.properties)

## 回归测试

![image.png](https://upload-images.jianshu.io/upload_images/4994935-3a7e8bd0e62d6d28.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 使用 @GlobalTransactional 注解

![image.png](https://upload-images.jianshu.io/upload_images/4994935-7bc06d3d08b6a9e0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

<span style="color: yellow; "> 
RestTemplate使用必须要以注入的方式使用 @Autowired
</span>

##### 测试疯狂点击

没有被扣除

![image.png](https://upload-images.jianshu.io/upload_images/4994935-cc9d0eaee41fbf9d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### [代码示例](https://github.com/targerr/distributed/tree/seata-restTemplate)

### 使用Fegin

##### pom

![image.png](https://upload-images.jianshu.io/upload_images/4994935-31e11fbd2c3d3654.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 启动类添加注解

![image.png](https://upload-images.jianshu.io/upload_images/4994935-9ecdb13217350655.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##### 图示

![image.png](https://upload-images.jianshu.io/upload_images/4994935-ee8de19af4491ade.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 测试 疯狂点击 完美

![image.png](https://upload-images.jianshu.io/upload_images/4994935-25f14b611b3a8532.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
