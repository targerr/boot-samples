## Sentinel 实践

### 1、简单使用

#### 1.1、 控制台部署步骤

- 访问: [下载](https://github.com/alibaba/Sentinel/releases) ,下载最新版Sentinel-Dashboard
- 利用下面的命令启动Dashboard

```shell
java -jar sentinel-dashboard-1.8.0.jar
# 修改端口号
java -jar -Dserver.port=9100 sentinel-dashboard-1.8.0.jar

```

- 访问本机8080端口,默认账户sentinel/sentinel

> http://localhost:8080

#### 1.2、 依赖配置

(1) pom依赖

```xml

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>


```

(2) 程序改造建立Dashboard通信

```yaml
    # sentinel通信
    sentinel:
      transport:
        # sentinel-dashboard-1.6.0 的访问路径 ，启动方式java -jar sentinel-dashboard-1.6.0.jar
        # java -Dserver.port=8082 -jar sentinel-dashboard-1.6.0.jar   默认8080端口
        # Sentinel 控制台地址
        dashboard: localhost:8080
        # 如果有多套网络，又无法正确获取本机IP，则需要使用下面的参数设置当前机器可被外部访问的IP地址，供admin控制台使用
        # spring.cloud.sentinel.transport.client-ip=
        # 应用服务 WEB 访问端口
        #client-ip: 172.16.22.197
        heartbeat-interval-ms: 500
      # 取消Sentinel控制台懒加载
      # 默认情况下 Sentinel 会在客户端首次调用的时候进行初始化，开始向控制台发送心跳包
      # 配置 sentinel.eager=true 时，取消Sentinel控制台懒加载功能
      eager: true

```

(3) 采用硬编码限流规则的

- 硬编码方式

```java

/**
 * <h1>流控规则硬编码的 Controller</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/code")
public class FlowRuleCodeController {

    /**
     * <h2>初始化流控规则</h2>
     * */
    @PostConstruct
    public void init() {

        // 流控规则集合
        List<FlowRule> flowRules = new ArrayList<>();
        // 创建流控规则
        FlowRule flowRule = new FlowRule();
        // 设置流控规则 QPS, 限流阈值类型 (QPS, 并发线程数)
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 流量控制手段
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        // 设置受保护的资源
        flowRule.setResource("flowRuleCode");
        // 设置受保护的资源的阈值
        flowRule.setCount(1);
        flowRules.add(flowRule);

        // 加载配置好的规则
        FlowRuleManager.loadRules(flowRules);
    }

    /**
     * <h2>采用硬编码限流规则的 Controller 方法</h2>
     * */
    @GetMapping("/flow-rule")
    @SentinelResource(value = "flowRuleCode")
    public CommonResponse<String> flowRuleCode() {
        log.info("request flowRuleCode");
        return new CommonResponse<>(0, "", "imooc-qinyi-ecommerce");
    }
}
```

- 流控兜底

```java
/**
 * <h1>流控规则硬编码的 Controller</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/code")
public class FlowRuleCodeController {
    /**
     * <h2>采用硬编码限流规则的 Controller 方法</h2>
     * */
    @GetMapping("/flow-rule")
    @SentinelResource(value = "flowRuleCode", blockHandler = "handleException")

    public CommonResponse<String> flowRuleCode() {
        log.info("request flowRuleCode");
        return new CommonResponse<>(0, "", "imooc-qinyi-ecommerce");
    }

    /**
     * <h2>当限流异常抛出时, 指定调用的方法</h2>
     * 是一个兜底策略
     * */
    public CommonResponse<String> handleException(BlockException exception) {
        log.error("has block exception: [{}]", JSON.toJSONString(exception.getRule()));
        return new CommonResponse<>(
                -1,
                "flow rule exception",
                exception.getClass().getCanonicalName()
        );
    }
}
```

- 自定义通用的限流处理逻辑

```java

/**
 * <h1>流控规则硬编码的 Controller</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/code")
public class FlowRuleCodeController {
    /**
     * <h2>采用硬编码限流规则的 Controller 方法</h2>
     * */
    @GetMapping("/flow-rule")
    @SentinelResource(
            value = "flowRuleCode", blockHandler = "qinyiHandleBlockException",
            blockHandlerClass = QinyiBlockHandler.class
    )
    public CommonResponse<String> flowRuleCode() {
        log.info("request flowRuleCode");
        return new CommonResponse<>(0, "", "imooc-qinyi-ecommerce");
    }

}
```

- 自定义通用的限流处理逻辑

```java
/**
 * <h1>自定义通用的限流处理逻辑</h1>
 * */
@Slf4j
public class QinyiBlockHandler {

    /**
     * <h2>通用限流处理方法</h2>
     * 这个方法必须是 static 的
     * */
    public static CommonResponse<String> qinyiHandleBlockException(BlockException exception) {

        log.error("trigger qinyi block handler: [{}], [{}]",
                JSON.toJSONString(exception.getRule()), exception.getRuleLimitApp());
        return new CommonResponse<>(
                -1,
                "flow rule trigger block exception",
                null
        );
    }
}

```

- 测试

```shell
### 硬编码的流控规则
GET 127.0.0.1:8100/ecommerce-sentinel-client/code/flow-rule
Content-Type: application/json

```

### 2、Sentinel 降级功能

#### 2.1、 支持 RestTemplate 流控

(1) 配置文件

```yaml
# 开启或关闭 @SentinelRestTemplate 注解
resttemplate:
  sentinel:
    enabled: true

```

(2) 对 RestTemplate 包装

```java
/**
 * <h1>开启服务间的调用保护, 需要给 RestTemplate 做一些包装</h1>
 * */
@Slf4j
@Configuration
public class SentinelConfig {

    /**
     * <h2>包装 RestTemplate</h2>
     * */
    @Bean
    @SentinelRestTemplate
//    (
//            fallback = "handleFallback", fallbackClass = RestTemplateExceptionUtil.class,
//            blockHandler = "handleBlock", blockHandlerClass = RestTemplateExceptionUtil.class
//    )
    public RestTemplate restTemplate() {
        return new RestTemplate();  // 可以对其做一些业务相关的配置
    }
}

```

(3) @SentinelRestTemplate fallback fallbackClass 指定类的异常和降级方法

```java

@Slf4j
@Configuration
public class SentinelConfig {

    @Bean
    @SentinelRestTemplate
            (
                    fallback = "handleFallback", fallbackClass = RestTemplateExceptionUtil.class,
                    blockHandler = "handleBlock", blockHandlerClass = RestTemplateExceptionUtil.class
            )
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

```

(4) 验证

```java

@Slf4j
@RestController
@RequestMapping("/sentinel-rest-template")
public class SentinelRestTemplateController {

    private final RestTemplate restTemplate;

    public SentinelRestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * <h2>从授权服务中获取 JwtToken</h2>
     * 1. 流控降级:
     * 是针对于簇点链路中的 http://127.0.0.1:7000/ecommerce-authority-center/authority/token
     * 2. 容错降级: 对于服务不可用时不能生效
     * */
    @PostMapping("/get-token")
    public JwtToken getTokenFromAuthorityService(
            @RequestBody UsernameAndPassword usernameAndPassword) {

        String requestUrl =
                "http://127.0.0.1:7000/ecommerce-authority-center/authority/token";
        log.info("RestTemplate request url and body: [{}], [{}]",
                requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
                JwtToken.class
        );
    }
}

```

(5) 测试

```shell
### 获取 Token
POST 127.0.0.1:8100/ecommerce-sentinel-client/sentinel-rest-template/get-token
Content-Type: application/json

{
  "username": "tom",
  "password": "25d55ad283aa400af464c76d713c07ad"
}

```

#### 2.2、 支持 Feign 流控

(1) pom依赖

```xml
<!-- Sentinel 适配了 Feign, 可以实现服务间调用的保护 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

(2) 配置文件

```yaml
# 打开 Sentinel 对 Feign 的支持
feign:
  sentinel:
    enabled: true


```

(3) 注解

```java
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SentinelClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(SentinelClientApplication.class, args);
    }
}

```

(4) 定义 client

```java
/**
 * <h1>通过 Sentinel 对 OpenFeign 实现熔断降级</h1>
 * */
@FeignClient(
        value = "e-commerce-imooc",
        fallback = SentinelFeignClientFallback.class
)
public interface SentinelFeignClient {

    @RequestMapping(value = "qinyi", method = RequestMethod.GET)
    CommonResponse<String> getResultByFeign(@RequestParam Integer code);
}

```

(5) 实现熔断降级

```java
/**
 * <h1>Sentinel 对 OpenFeign 接口的降级策略</h1>
 * */
@Slf4j
@Component
public class SentinelFeignClientFallback implements SentinelFeignClient {

    @Override
    public CommonResponse<String> getResultByFeign(Integer code) {

        log.error("request supply for test has some error: [{}]", code);
        return new CommonResponse<>(
                -1,
                "sentinel feign fallback",
                "input code: "+ code
        );
    }
}

```

(6) 验证

```java
/**
 * <h1>OpenFeign 集成 Sentinel 实现熔断降级</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/sentinel-feign")
public class SentinelFeignController {

    private final SentinelFeignClient sentinelFeignClient;

    public SentinelFeignController(SentinelFeignClient sentinelFeignClient) {
        this.sentinelFeignClient = sentinelFeignClient;
    }

    /**
     * <h2>通过 Feign 接口去获取结果</h2>
     * */
    @GetMapping("/result-by-feign")
    public CommonResponse<String> getResultByFeign(@RequestParam Integer code) {
        log.info("coming in get result by feign: [{}]", code);
        return sentinelFeignClient.getResultByFeign(code);
    }
}


```

(7) 控制台配置流控规则,进行验证

```shell
### Sentinel Feign Client
GET 127.0.0.1:8100/ecommerce-sentinel-client/sentinel-feign/result-by-feign?code=200
Content-Type: application/json

```

(8) 全局异常捕获

```java
package com.example.handler;

/**
 * @Author: wgs
 * @Date 2022/11/29 16:58
 * @Classname SentinelHandler
 * @Description 自定义 Sentinel 异常信息
 */
@Component
@Slf4j
public class SentinelHandler implements BlockExceptionHandler {
    /**
     * 流控与异常信息处理器
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws Exception
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        String msg = null;

        if (e instanceof FlowException) {//限流异常
            msg = "接口已被限流";
        } else if (e instanceof DegradeException) {//熔断异常
            msg = "接口已被熔断,请稍后再试";
        } else if (e instanceof ParamFlowException) { //热点参数限流
            msg = "热点参数限流"; //例如:id参数=5不开启限流,id=10开启限流,针对不同的参数进行不同的限流策略
        } else if (e instanceof SystemBlockException) { //系统规则异常
            msg = "系统规则(负载/....不满足要求)";//例如:ＣＰＵ负载超过８０％则不允许访问
        } else if (e instanceof AuthorityException) { //授权规则异常
            msg = "授权规则不通过"; //例如:服务A不允许服务B进行访问,服务B当发起调用后就会触发授权异常
        }

        CommonResponse<String> response = new CommonResponse<String>(429, msg, "");

        httpServletResponse.setStatus(429);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");

        httpServletResponse.getWriter().write(JSONObject.toJSONString(response));
    }

}

```

### 3、 Sentinel 整合 Nacos 持久化

### 3.1、pom 依赖

```xml
     <!-- Sentinel 使用 Nacos 存储规则 -->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
```

### 3.2、配置文件

```json
[
    {
        "resource":"/list", #资源名
        "limitApp":"default", #来源
        "grade":1, #类型 0-线程 1-QPS
        "count":2, #超过2个QPS限流
        "strategy":0, #限流策略: 0-直接 1-关联 2-链路
        "controlBehavior":0, #控制行为: 0-快速失败 1-warmup 2-排队等待
        "clusterMode":false #集群模式
    },{
        "resource":"GET:http://video-service/video",
        "limitApp":"default",
        "grade":0,
        "count":10,
        "strategy":0,
        "controlBehavior":0,
        "clusterMode":false
    }
]
```

#### 3.3、Nacos 限流配置

- DEFAULT_GROUP
- e-commerce-sentinel-client-sentinel

![image.png](https://upload-images.jianshu.io/upload_images/4994935-c4cf05b5dbff1abf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 3.4、 增加nacos dataid配置

```yaml
spring:
  application:
    name: e-commerce-sentinel-client # 应用名称也是构成 Nacos 配置管理 dataId 字段的一部分 (当 config.prefix 为空时)
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
        server-addr: 127.0.0.1:8848
        #  server-addr: 127.0.0.1:8848,127.0.0.1:8849,127.0.0.1:8850 # Nacos 服务器地址
        namespace: 1bc13fd5-843b-4ac0-aa55-695c25bc0ac6
        metadata:
          management:
            context-path: ${server.servlet.context-path}/actuator
    sentinel:
      # 配置 sentinel dashboard 地址
      transport:
        dashboard: 127.0.0.1:7777
        port: 8719 # 会在应用对应的机器上启动一个 Http Server, 该 Server 会与 Sentinel 控制台做交互
      datasource:
        # 名称任意, 代表数据源
        ds:
          nacos:
            # NacosDataSourceProperties.java 中定义
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name}-sentinel
            namespace: ${spring.cloud.nacos.discovery.namespace}
            groupId: DEFAULT_GROUP
            data-type: json
            # 规则类型: com.alibaba.cloud.sentinel.datasource.RuleType
            # FlowRule 就是限流规则
            rule-type: flow
      # 服务启动直接建立心跳连接
      eager: true
```

### 4、 Gateway 集成 Sentinel 限流

#### 4.1 硬编码配置

(1) pom 依赖

```xml

    <dependencies>
        <!-- spring cloud alibaba nacos discovery 依赖 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <version>2.2.3.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <!-- 集成 Sentinel, 在网关层面实现限流 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
        </dependency>
        <!-- Sentinel 使用 Nacos 存储规则 -->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
    </dependencies>
```

(2) 配置文件

```yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    nacos:
      discovery:
        enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
        server-addr: 172.16.200.249:8848 # Nacos 服务器地址
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7
#        metadata:
#          management:
#            context-path: ${server.servlet.context-path}/actuator
    sentinel:
      eager: true
      transport:
        port: 8720
        dashboard: 127.0.0.1:7777
```

(3) Sentinel 启动控制台 **整合 gateway 需指定参数type=1**

```shell
java -Dserver.port=8718 -Dcsp.sentinel.app.teype=1 -Dcsp.sentinel.dashboard.server=localhost:8718 -Dproject.name=sentinel-dashboard -Dcsp.sentinel.api.port=8719  -jar sentinel-dashboard-1.6.2.jar
```

(4) 硬编码(大颗粒度)

```java

/**
 * <h1>Gateway 集成 Sentinel 实现限流</h1>
 * */
@Slf4j
@Configuration
public class SentinelGatewayConfiguration1 {

    /** 视图解析器 */
    private final List<ViewResolver> viewResolvers;
    /** HTTP 请求和响应数据的编解码配置 */
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * <h2>构造方法</h2>
     * */
    public SentinelGatewayConfiguration1(
            ObjectProvider<List<ViewResolver>> viewResolversProvider,
            ServerCodecConfigurer serverCodecConfigurer
    ) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * <h2>限流异常处理器, 限流异常出现时, 执行到这个 handler</h2>
     * */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // 默认会返回错误 message, code 429
        return new SentinelGatewayBlockExceptionHandler(
                this.viewResolvers,
                this.serverCodecConfigurer
        );
    }

    /**
     * <h2>限流过滤器, 是 Gateway 全局过滤器, 优先级定义为最高</h2>
     * */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * <h2>初始化限流规则</h2>
     * */
    @PostConstruct
    public void doInit() {

        log.info("---------------------------------------------------");

        // 加载网关限流规则
        log.info("load sentinel gateway rules (code define)");
        initGatewayRules();

        // 加载自定义限流异常处理器
        initBlockHandler();

        log.info("---------------------------------------------------");
    }

    /**
     * <h2>硬编码网关限流规则</h2>
     * */
    private void initGatewayRules() {

        Set<GatewayFlowRule> rules = new HashSet<>();

        GatewayFlowRule rule = new GatewayFlowRule();
        // 指定限流模式, 根据 route_id 做限流, 默认的模式
        rule.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID);
        // 指定 route_id -> service id
        rule.setResource("e-commerce-nacos-client");
        // 按照 QPS 限流
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 统计窗口和限流阈值
        rule.setIntervalSec(60);
        rule.setCount(3);

        rules.add(rule);

        // 加载到网关中
        GatewayRuleManager.loadRules(rules);

        // 加载限流分组
        initCustomizedApis();
    }

    /**
     * <h2>自定义限流异常处理器</h2>
     * */
    private void initBlockHandler() {

        // 自定义 BlockRequestHandler
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange,
                                                      Throwable throwable) {

                log.error("------------- trigger gateway sentinel rule -------------");

                Map<String, String> result = new HashMap<>();
                result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
                result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
                result.put("route", "e-commerce-nacos-client");

                return ServerResponse
                        .status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(result));
            }
        };

        // 设置自定义限流异常处理器
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    /**
     * <h2>硬编码网关限流分组</h2>
     * 1. 最大限制 - 演示
     * 2. 具体的分组
     * */
    private void initCustomizedApis() {

        Set<ApiDefinition> definitions = new HashSet<>();

        // nacos-client-api 组, 最大的限制
        ApiDefinition api = new ApiDefinition("nacos-client-api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    // 模糊匹配 /imooc/ecommerce-nacos-client/ 及其子路径的所有请求
                    add(new ApiPathPredicateItem()
                            .setPattern("/imooc/ecommerce-nacos-client/**")
                            // 根据前缀匹配
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

     
        definitions.add(api);

        // 加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
```

(5) 针对某个接口

```java

/**
 * <h1>Gateway 集成 Sentinel 实现限流</h1>
 * */
@Slf4j
@Configuration
public class SentinelGatewayConfiguration1 {

    /** 视图解析器 */
    private final List<ViewResolver> viewResolvers;
    /** HTTP 请求和响应数据的编解码配置 */
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * <h2>构造方法</h2>
     * */
    public SentinelGatewayConfiguration1(
            ObjectProvider<List<ViewResolver>> viewResolversProvider,
            ServerCodecConfigurer serverCodecConfigurer
    ) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * <h2>限流异常处理器, 限流异常出现时, 执行到这个 handler</h2>
     * */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // 默认会返回错误 message, code 429
        return new SentinelGatewayBlockExceptionHandler(
                this.viewResolvers,
                this.serverCodecConfigurer
        );
    }

    /**
     * <h2>限流过滤器, 是 Gateway 全局过滤器, 优先级定义为最高</h2>
     * */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * <h2>初始化限流规则</h2>
     * */
    @PostConstruct
    public void doInit() {

        log.info("---------------------------------------------------");

        // 加载网关限流规则
        log.info("load sentinel gateway rules (code define)");
        initGatewayRules();

        // 加载自定义限流异常处理器
        initBlockHandler();

        log.info("---------------------------------------------------");
    }

    /**
     * <h2>硬编码网关限流规则</h2>
     * */
    private void initGatewayRules() {

        Set<GatewayFlowRule> rules = new HashSet<>();
      

        // 限流分组, Sentinel 先去找规则定义, 再去找规则中定义的分组
        rules.add(
                new GatewayFlowRule("nacos-client-api-1")
                        .setCount(3).setIntervalSec(60)
        );
        rules.add(
                new GatewayFlowRule("nacos-client-api-2")
                        .setCount(1).setIntervalSec(60)
        );

        // 加载到网关中
        GatewayRuleManager.loadRules(rules);

        // 加载限流分组
        initCustomizedApis();
    }

    /**
     * <h2>自定义限流异常处理器</h2>
     * */
    private void initBlockHandler() {

        // 自定义 BlockRequestHandler
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange,
                                                      Throwable throwable) {

                log.error("------------- trigger gateway sentinel rule -------------");

                Map<String, String> result = new HashMap<>();
                result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
                result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
                result.put("route", "e-commerce-nacos-client");

                return ServerResponse
                        .status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(result));
            }
        };

        // 设置自定义限流异常处理器
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    /**
     * <h2>硬编码网关限流分组</h2>
     * 1. 最大限制 - 演示
     * 2. 具体的分组
     * */
    private void initCustomizedApis() {

        Set<ApiDefinition> definitions = new HashSet<>();
      

        // nacos-client-api-1 分组
        ApiDefinition api1 = new ApiDefinition("nacos-client-api-1")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem()
                            // 精确匹配 /imooc/ecommerce-nacos-client/nacos-client/service-instance
                            .setPattern("/imooc/ecommerce-nacos-client" +
                                    "/nacos-client/service-instance"));
                }});

        // nacos-client-api-2 分组
        ApiDefinition api2 = new ApiDefinition("nacos-client-api-2")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem()
                            // 精确匹配 /imooc/ecommerce-nacos-client/nacos-client/project-config
                            .setPattern("/imooc/ecommerce-nacos-client" +
                                    "/nacos-client/project-config"));
                }});

        definitions.add(api1);
        definitions.add(api2);

        // 加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}

```

#### 4.2 通过本地配置文件

(1) 注释硬编码加载

```java
    /**
     * <h2>初始化限流规则</h2>
     * */
//    @PostConstruct
    public void doInit() {}
```

(2) 修改配置文件

```yaml
    sentinel:
      eager: true
      transport:
        port: 8720
        dashboard: 127.0.0.1:7777
      datasource:
        # 通过本地文件方式, 基于服务级别的配置
        dsl.file:
          file: classpath:gateway-flow-rule-sentinel.json
          # 代表服务级别的限流, 一步步点进去看, 文件类型
          ruleType: gw-flow
        # 通过本地文件方式, 细粒度对指定 api 进行配置
        ds2.file:
          file: classpath:gateway-flow-rule-api-sentinel.json
          # 代表 API 分组, 一步步点进去看, 文件类型
          ruleType: gw-api-group
```

(3) gateway-flow-rule-sentinel.json

```json

[
  {
    "resource": "e-commerce-nacos-client",
    "resourceMode": 0,
    "count": 3,
    "intervalSec": 60
  },
  {
    "resource": "nacos-client-api",
    "resourceMode": 1,
    "count": 1,
    "intervalSec": 60
  }
]

```

(4) gateway-flow-rule-api-sentinel.json

```json
[
  {
    "apiName": "nacos-client-api",
    "predicateItems": [
      {
        "pattern": "/imooc/ecommerce-nacos-client/nacos-client/project-config"
      },
      {
        "pattern": "/imooc/ecommerce-nacos-client/**",
        "matchStrategy": 1
      }
    ]
  }
]

```

#### 4.3 通过 nacose 配置

(1) 配置文件

```yaml
    sentinel:
      eager: true
      transport:
        port: 8720
        dashboard: 127.0.0.1:7777
      datasource:
        # 通过本地文件方式, 基于服务级别的配置
#        dsl.file:
#          file: classpath:gateway-flow-rule-sentinel.json
#          # 代表服务级别的限流, 一步步点进去看, 文件类型
#          ruleType: gw-flow
#        # 通过本地文件方式, 细粒度对指定 api 进行配置
#        ds2.file:
#          file: classpath:gateway-flow-rule-api-sentinel.json
#          # 代表 API 分组, 一步步点进去看, 文件类型
#          ruleType: gw-api-group
        # 集成 Nacos
        ds1:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            namespace: ${spring.cloud.nacos.discovery.namespace}
            # 测试时, 看看 Nacos 中修改是否能让 dashboard 生效, 就把第二个 count 也修改为 3
            data-id: gateway-flow-rule-sentinel
            group-id: DEFAULT_GROUP
            data-type: json
            rule-type: gw-flow
        ds2:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            namespace: ${spring.cloud.nacos.discovery.namespace}
            data-id: gateway-flow-rule-api-sentinel
            group-id: DEFAULT_GROUP
            data-type: json
            rule-type: gw-api-group
```

(2) 本地配置文件放入 nacos 中
![image.png](https://upload-images.jianshu.io/upload_images/4994935-d5816636867e5b20.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
