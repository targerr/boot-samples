### Gateway 路由

#### 一. 添加依赖

```xml

<dependencies>
    <!-- spring cloud alibaba nacos discovery 依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--网关依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

</dependencies>
```

**注意:**

> spring cloud gateway使用的web框架为webflux，和springMVC不兼容.无需配置 spring-boot-starter-web 依赖

#### 二. 配置

- 动态路由配置
  ```yaml
  server:
    port: 9001
    servlet:
      context-path: /gateway

  spring:
    application:
      name: commerce-gateway
    cloud:
      nacos:
        discovery:
          enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
          server-addr: 172.16.200.249:8848
          #server-addr: 127.0.0.1:8848
          # server-addr: 127.0.0.1:8848,127.0.0.1:8849,127.0.0.1:8850 # Nacos 服务器地址
          namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7
  #        metadata:
  #          management:
  #            context-path: ${server.servlet.context-path}/actuator


  # 这个地方独立配置, 是网关的数据, 代码 GatewayConfig.java 中读取被监听
  nacos:
    gateway:
      route:
        config:
          data-id: commerce-gateway-router
          group: commerce

  # 暴露端点
  management:
    endpoints:
      web:
        exposure:
          include: '*'
    endpoint:
      health:
        show-details: always
  logging:
    level:
      com.example: debug
      org.springframework.cloud.gateway: trace
  ```

* 静态路由配置

```yaml
server:
  port: 9001
  servlet:
    context-path: /gateway

spring:
  application:
    name: commerce-gateway
  cloud:
    nacos:
      discovery:
        enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
        server-addr: 172.16.200.249:8848
        #server-addr: 127.0.0.1:8848
        # server-addr: 127.0.0.1:8848,127.0.0.1:8849,127.0.0.1:8850 # Nacos 服务器地址
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7
    #        metadata:
    #          management:
    #            context-path: ${server.servlet.context-path}/actuator
    # 静态路由
    gateway:
      discovery:
        locator:
          enabled: true # 让gateway可以发现nacos中的微服务
      routes:
        - id: product_route # 路由的名字
          uri: lb://commerce-nacos-client # lb指的是从nacos中按照名称获取微服务,并遵循负载均衡策略
          predicates:
            - Path=/nacos-s/** # 符合这个规定的才进行1转发
          filters:
            - StripPrefix=1 # 将第一层去掉
            - Time=true
            - HeaderToken=false
        - id: commerce-authority # 路由的名字
          uri: lb://commerce-authority-center # lb指的是从nacos中按照名称获取微服务,并遵循负载均衡策略
          predicates:
            - Path=/authority/** # 符合这个规定的才进行1转发
          filters:
            - StripPrefix=1 # 将第一层去掉


```
- 配置类 网关
```java

@Configuration
public class RouteLocatorConfig {

    /**
     * <h2>使用代码定义路由规则, 在网关层面拦截下登录和注册接口</h2>
     */
    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {

        // 手动定义 Gateway 路由规则需要指定 id、path 和 uri
        return builder.routes()
                .route(
                        "e-commerce_authority",
                        r -> r.path(
                                "/gateway/e-commerce/login",
                                "/gateway/e-commerce/register"
                        ).uri("http://localhost:9002/")
                ).build();
    }
}

```

#### 三. 配置类

1. 从nacos加载动态路由配置。**读取Nacos的命名空间namespace，通过dataId获取配置**
   ```java

   /***
   # 这个地方独立配置, 是网关的数据, 代码 GatewayConfig.java 中读取被监听
   nacos:
     gateway:
       route:
         config:
           data-id: commerce-gateway-router
           group: commerce
   */


   @Configuration
   public class GatewayConfig {

       /** 读取配置的超时时间 */
       public static final long DEFAULT_TIMEOUT = 30000;

       /** Nacos 服务器地址 */
       public static String NACOS_SERVER_ADDR;

       /** 命名空间 */
       public static String NACOS_NAMESPACE;

       /** data-id */
       public static String NACOS_ROUTE_DATA_ID;

       /** 分组 id */
       public static String NACOS_ROUTE_GROUP;

       @Value("${spring.cloud.nacos.discovery.server-addr}")
       public void setNacosServerAddr(String nacosServerAddr) {
           NACOS_SERVER_ADDR = nacosServerAddr;
       }

       @Value("${spring.cloud.nacos.discovery.namespace}")
       public void setNacosNamespace(String nacosNamespace) {
           NACOS_NAMESPACE = nacosNamespace;
       }

       @Value("${nacos.gateway.route.config.data-id}")
       public void setNacosRouteDataId(String nacosRouteDataId) {
           NACOS_ROUTE_DATA_ID = nacosRouteDataId;
       }

       @Value("${nacos.gateway.route.config.group}")
       public void setNacosRouteGroup(String nacosRouteGroup) {
           NACOS_ROUTE_GROUP = nacosRouteGroup;
       }
   }

   ```

- 如图
  ![image.png](https://upload-images.jianshu.io/upload_images/4994935-ec97b31f6a475b09.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 动态更新路由网关service

```java


/**
 * 动态更新路由网关service
 * 1）实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
 * 2）提供动态路由的基础方法，可通过获取bean操作该类的方法。该类提供新增路由、更新路由、删除路由，然后实现发布的功能。
 */
@Slf4j
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    /**
     * 发布事件
     */
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    public String delete(String id) {
        try {
            log.info("gateway delete route id {}", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception e) {
            return "delete fail";
        }
    }

    /**
     * 更新路由
     * @param definitions
     * @return
     */
    public String updateList(List<RouteDefinition> definitions) {
        log.info("gateway update route {}", definitions);
        // 删除缓存routerDefinition
        List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            routeDefinitionsExits.forEach(routeDefinition -> {
                log.info("delete routeDefinition:{}", routeDefinition);
                delete(routeDefinition.getId());
            });
        }
        definitions.forEach(definition -> {
            updateById(definition);
        });
        return "success";
    }

    /**
     * 更新路由
     * @param definition
     * @return
     */
    public String updateById(RouteDefinition definition) {
        try {
            log.info("gateway update route {}", definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    /**
     * 增加路由
     * @param definition
     * @return
     */
    public String add(RouteDefinition definition) {
        log.info("gateway add route {}", definition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }
}


```

3.通过nacos下发动态路由配置,监听Nacos中gateway-route配置****

```java

@Component
@Slf4j
@DependsOn({"gatewayConfig"}) // 依赖于gatewayConfig bean
public class DynamicRouteServiceImplByNacos {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    private ConfigService configService;


    /**
     * <h2>Bean 在容器中构造完成之后会执行 init 方法</h2>
     * */
    @PostConstruct
    public void init() {

        log.info("gateway route init....");

        try {
            // 初始化 Nacos 配置客户端
            configService = initConfigService();
            if (null == configService) {
                log.error("init config service fail");
                return;
            }

            // 通过 Nacos Config 并指定路由配置路径去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );

            log.info("get current gateway config: [{}]", configInfo);
            List<RouteDefinition> definitionList =
                    JSON.parseArray(configInfo, RouteDefinition.class);

            if (CollectionUtils.isNotEmpty(definitionList)) {
                for (RouteDefinition definition : definitionList) {
                    log.info("init gateway config: [{}]", definition.toString());
                    dynamicRouteService.add(definition);
                }
            }

        } catch (Exception ex) {
            log.error("gateway route init has some error: [{}]", ex.getMessage(), ex);
        }

        // 设置监听器
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID,
                GatewayConfig.NACOS_ROUTE_GROUP);
    }

    /**
     * <h2>初始化 Nacos Config</h2>
     * */
    private ConfigService initConfigService() {

        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception ex) {
            log.error("init gateway nacos config error: [{}]", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * <h2>监听 Nacos 下发的动态路由配置</h2>
     * */
    private void dynamicRouteByNacosListener(String dataId, String group) {

        try {
            // 给 Nacos Config 客户端增加一个监听器
            configService.addListener(dataId, group, new Listener() {

                /**
                 * <h2>自己提供线程池执行操作</h2>
                 * */
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * <h2>监听器收到配置更新</h2>
                 * @param configInfo Nacos 中最新的配置定义
                 * */
                @Override
                public void receiveConfigInfo(String configInfo) {

                    log.info("start to update config: [{}]", configInfo);
                    List<RouteDefinition> definitionList =
                            JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route: [{}]", definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }
            });
        } catch (NacosException ex) {
            log.error("dynamic update gateway config error: [{}]", ex.getMessage(), ex);
        }
    }
}

```

#### 四. 测试验证

- 测试数据

```yaml

[
  {
    "filters": [
      {
        "args": {
          "parts": "1"
        },
        "name": "StripPrefix"
      },
      {
        "args": {
          "parts": "true"
        },
        "name": "Time"
      }
    ],
    "id": "commerce-nacos-client",
    "order": 0,
    "predicates": [
      {
        "args": {
          "pattern": "/nacos-s/**"
        },
        "name": "Path"
      }
    ],
    "uri": "lb://commerce-nacos-client"
  }
]

```

- 修改配置文件触发动态更新路由 nacos 配置文件
  ![image.png](https://upload-images.jianshu.io/upload_images/4994935-32dbab610d4e6ab9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  idea 控制台
  ![image.png](https://upload-images.jianshu.io/upload_images/4994935-cd2318a282d66d9a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 访问测试
  链接: http://127.0.0.1:9001/nacos-client/ecommerce-nacos-client/nacos-client/service-instancehttp://127.0.0.1:9001/nacos-client/ecommerce-nacos-client/nacos-client/service-instance
- 如图
  ![image.png](https://upload-images.jianshu.io/upload_images/4994935-5a15214a0a34858e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





#### 五、总结

　　1）Spring Cloud Gateway作用不光只是简单的跳转重定向，还可以**实现用户的验证登录，解决跨域，日志拦截，权限控制，限流，熔断，负载均衡，黑名单和白名单机制等。是微服务架构不二的选择**；

　　2） **Nacos的配置中心支持动态获取配置文件，可以将一些全局的经常变更的配置文件放在Nacos下** ，需要到微服务自行获取。

[参考文档](https://www.cnblogs.com/jian0110/p/12862569.html)

[强烈推荐](https://mp.weixin.qq.com/s/lyEbaMWmt0LSDG70BcV1sw)
