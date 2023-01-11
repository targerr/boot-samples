### Gateway 过滤器
> 全局过滤器、局部过滤器

#### 一. 局部过滤器

1. 自定义Gateway Filter
   实现自定义的Gateway Filter我们需要GatewayFilter、Ordered两个接口
```java
@Component
@Slf4j
public class TimeGatewayFilterFactory extends AbstractGatewayFilterFactory<TimeGatewayFilterFactory.Config> {

    private static final String BEGIN_TIME = "beginTime";

    //构造函数
    public TimeGatewayFilterFactory() {
        super(Config.class);
    }

    //读取配置文件中的参数 赋值到 配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("show");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.error("耗时局部过滤器执行~~~~");
                if (!config.show) {
                    // 如果配置类中的show为false，表示放行
                    return chain.filter(exchange);
                }
                exchange.getAttributes().put(BEGIN_TIME, System.currentTimeMillis());
                /**
                 *  pre的逻辑
                 * chain.filter().then(Mono.fromRunable(()->{
                 *     post的逻辑
                 * }))
                 */
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(BEGIN_TIME);
                    if (startTime != null) {
                        log.info("【Gateway】 接口地址:{} 耗时: {} ms", exchange.getRequest().getURI(), (System.currentTimeMillis() - startTime));
                    }
                }));
            }
        };
    }

    @Setter
    @Getter
    static class Config {
        private boolean show;
    }

}
```
2. 在application.yml配置使用
```yaml
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
```

#### 二. 全局过滤器
1. 自定义Global Filter
```java

/**
 * <h1>全局接口耗时日志过滤器</h1>
 */
@Slf4j
@Component
public class GlobalElapsedLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 前置逻辑
        StopWatch sw = StopWatch.createStarted();
        String uri = exchange.getRequest().getURI().getPath();

        return chain.filter(exchange).then(
                // 后置逻辑
                Mono.fromRunnable(() ->
                        log.info("【Gateway】 地址:[{}] 耗时: [{}ms]", uri, sw.getTime(TimeUnit.MILLISECONDS)))
        );
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}

```