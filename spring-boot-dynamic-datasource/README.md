# 自定义多数据源方案

## 本文主要涉及以下知识点:

- 动态数据源切换
- AOP
- mybatis拦截器
- 上下文
- 函数式方法
- 自定义配置加载解析

## 方案设计

基础知识点
主要借助AbstractRoutingDataSource来实现动态数据源，简单介绍下它的几个核心要点：

- defaultTargetDataSource：表示默认使用的数据源
- Map<Object, Object> targetDataSources: 动态数据源可以根据key来切换使用的数据源

```java
protected Object determineCurrentLookupKey() {
    // 返回上面 targetDataSources 对应的key
    // 根据这个key，来抉择最终实际选择的数据源时哪个
}
```

重点关注上面的这个方法，当我们想使用哪个数据源时，就发回这个数据源对应的key，在实际代码执行时，就根据这个key，到 targetDataSources这个map中找对应的数据源

## 设计方案

基于上面的动态数据源的几个核心知识点，所以当我们需要实现动态数据源切换时，自然而然可以想到的一个方案就是在每次db执行之前，塞入这个希望使用的数据源key

![d82673ae-8d87-48ba-8fbc-288ebdcb237b.png](https://upload-images.jianshu.io/upload_images/4994935-4b696530e894729b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如上的设计思路，主要借助aop的思想来实现，通过上下文来存储当前方法的执行，具体希望使用的数据源，然后再执行的sql的时候，AbstractRoutingDataSource直接从上下文中获取key，以此来抉择具体的数据源

基于上面的方案，接下来我们补充一下细节

1. 数据源选择方式


通过AOP的方式进行拦截，写入数据源选择，这种方式适用于方法级别的粒度，基于此常见的实现方式就是通过自定义注解，来标注需要使用的数据源

如自定义注解，通过设置ds值来指定这个方法执行时选择的数据源

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DsAno {
    /**
     * 启用的数据源
     *
     * @return
     */
    String ds() default "";
}
```

但是，请注意上面这种方式不适用于代码块维度的数据源选择策略



2. 上下文


   通过上下文来保存当前选中的数据源，因此可以借助 ThreadLocal 来实现

一个简单的数据源选择上下文

```java
public class DsContextHolder{
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
}
```

3. 动态数据源选择

自定义的动态数据源路由方案

```java
protected Object determineCurrentLookupKey() {
    return DsContextHolder.get();
}
```

4. aop拦截

借助AOP，拦截方法、类上拥有DS注解的方法执行，然后从这个注解中读取选中的数据源，然后写入上下文；再方法执行完毕之后，清空上下文

基本的实现策略

```java
/**
 * 切入点, 拦截类上、方法上有注解的方法，用于切换数据源
 */
@Pointcut("@annotation(com.github.paicoding.forum.core.dal.DsAno) || @within(com.github.paicoding.forum.core.dal.DsAno)")
public void pointcut() {
}

@Around("pointcut()")
public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    DsAno ds = getDsAno(proceedingJoinPoint);
    try {
        if (ds != null && (StringUtils.isNotBlank(ds.ds()) || ds.value() != null)) {
            // 当上下文中没有时，则写入线程上下文，应该用哪个DB
            DsContextHolder.set(StringUtils.isNoneBlank(ds.ds()) ? ds.ds() : ds.value().name());
        }
        return proceedingJoinPoint.proceed();
    } finally {
        // 清空上下文信息
        if (ds != null) {
            DsContextHolder.reset();
        }
    }
}

private DsAno getDsAno(ProceedingJoinPoint proceedingJoinPoint) {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Method method = signature.getMethod();
    DsAno ds = method.getAnnotation(DsAno.class);
    if (ds == null) {
        // 获取类上的注解
        ds = (DsAno) proceedingJoinPoint.getSignature().getDeclaringType().getAnnotation(DsAno.class);
    }
    return ds;
}
```

## 多数据源实现

1. 多数据源定义
   对于多数据源，因此无法直接使用默认的数据源配置，我们借助默认的配置规则，加一个多数据源版本的设计

对应的多数据源的配置规则如下

```yml
spring:
  dynamic: # 动态数据源
    primary: master # 这个表示默认的数据源
    datasource:
      master:
        # 数据库名，从配置 database.name 中获取
        url: jdbc:mysql://localhost:3306/${database.name}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
      slave:
        # 数据库名，从配置 database.name 中获取
        url: jdbc:mysql://localhost:3306/${database.name}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: readonly
        password:
```

注意上面的定义：

spring.dynamic.primary: 指定默认启用的数据源
--------------------------------------------

- spring.dynamic.datasource.数据源名.配置: 这里的数据源名不能重复（忽略大小写），数据源下的配置与默认的配置参数要求一致
- 对应的数据源配置加载方式直接借助了Spring的ConfigurationProperties来实现（注意：这是个知识点）

```java
@Data
@ConfigurationProperties(prefix = "spring.dynamic")
public class DsProperties {
    /**
     * 默认数据源
     */
    private String primary;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceProperties> datasource;
}
```

2. 多数据源注册

上面是数据源的配置定义与加载，但是我们需要基于上面的数据源配置来实例化对应的datasource

核心实现再 com.github.paicoding.forum.core.dal.DataSourceConfig



```java
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.dynamic", name = "primary")
@EnableConfigurationProperties(DsProperties.class)
public class DataSourceConfig {

    public DataSourceConfig() {
        log.info("动态数据源初始化!");
    }

    @Bean
    public DsAspect dsAspect() {
        return new DsAspect();
    }

/**
     * 整合主从数据源
     *
     * @param dsProperties
     * @return 1
     */
    @Bean
    @Primary
    public DataSource dataSource(DsProperties dsProperties) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(dsProperties.getDatasource().size());
        dsProperties.getDatasource().forEach((k, v) -> targetDataSources.put(k.toUpperCase(), v.initializeDataSourceBuilder().build()));

        if (CollectionUtils.isEmpty(targetDataSources)) {
            throw new IllegalStateException("多数据源配置，请以 spring.dynamic 开头");
        }

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        Object key = dsProperties.getPrimary().toUpperCase();
        if (!targetDataSources.containsKey(key)) {
            if (targetDataSources.containsKey(MasterSlaveDsEnum.MASTER.name())) {
                // 当们没有配置primary对应的数据源时，存在MASTER数据源，则将主库作为默认的数据源
                key = MasterSlaveDsEnum.MASTER.name();
            } else {
                key = targetDataSources.keySet().iterator().next();
            }
        }

        log.info("动态数据源，默认启用为： " + key);
        myRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get(key));
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }
}
```



上面的实现比较简单，但是有几个关键信息给大家指出一下

1. @ConditionalOnProperty(prefix = "spring.dynamic", name = "primary")

这是一个条件注入的限定，表示只有配置了动态数据源时，才会生效，即当前的技术派系统支持单数据源以及多数据源两种方式自有切换的

如果我只想使用单数据源的模式（默认场景）

![image.png](https://upload-images.jianshu.io/upload_images/4994935-93026b2a1d9f89a4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

当我们希望开启多数据源时，将上图中的注释打开即可

2.datasource实例化方式

```java
DataSourceProperties.initializeDataSourceBuilder().build()
```

通过上面的方式创建的DataSource为默认的HikariDataSource

因此当你的项目之前使用的是Druid时，请注意，这里就需要进行调整了（那么怎么调整呢？）

3.自定义数据源

```java
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DsContextHolder.get();
    }
}
```

### 3. 上下文定义


我们通过上下文来保存当前选择的数据源，因此一个简单的的上下文设计如下

```java
public class DsContextHolder {
    /**
     * 使用继承的线程上下文，支持异步时选择传递
     * 使用DsNode，支持链式的数据源切换，如最外层使用master数据源，内部某个方法使用slave数据源；但是请注意，对于事务的场景，不要交叉
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    private DsContextHolder() {
    }


    public static void set(String dbType) {
        CONTEXT_HOLDER.set(dyType);
    }

    public static String get() {
        return CONTEXT_HOLDER.get();
    }

    public static void set(DS ds) {
        set(ds.name().toUpperCase());
    }

    /**
     * 移除上下文
     */
    public static void reset() {
        CONTEXT_HOLDER.remove();
    }
}
```

注意：若发现上面的设计与代码中不一致的，无需疑问，因为本文介绍到是第一版的实现方式，代码的为改进后方案

### 4.AOP实现

借助AOP来简化数据源的选择，因此我们先定义一个注解DsAno，可以放在类上，表示这个类下所有的共有方法，都走某个数据源；也可以放在方法上，且方法上的优先级大于类上的注解

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DsAno {
    /**
     * 启用的数据源，默认主库
     *
     * @return
     */
    MasterSlaveDsEnum value() default MasterSlaveDsEnum.MASTER;

    /**
     * 启用的数据源，如果存在，则优先使用它来替换默认的value
     *
     * @return
     */
    String ds() default "";
}
```

上面的设计中，针对常见的主从切换做了一个简单的兼容，定义了一个主从数据源的枚举，同样也是基于简化使用体验的出发

```java
public interface DS {
    /**
     * 使用的数据源名
     *
     * @return
     */
    String name();
}
public enum MasterSlaveDsEnum implements DS {
    /**
     * master主数据源类型
     */
    MASTER,
    /**
     * slave从数据源类型
     */
    SLAVE;
}
```

具体的aop拦截实现方式

```java
@Aspect
public class DsAspect {
    /**
     * 切入点, 拦截类上、方法上有注解的方法，用于切换数据源
     */
    @Pointcut("@annotation(com.github.paicoding.forum.core.dal.DsAno) || @within(com.github.paicoding.forum.core.dal.DsAno)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DsAno ds = getDsAno(proceedingJoinPoint);
        try {
            if (ds != null && (StringUtils.isNotBlank(ds.ds()) || ds.value() != null)) {
                // 当上下文中没有时，则写入线程上下文，应该用哪个DB
                DsContextHolder.set(StringUtils.isNoneBlank(ds.ds()) ? ds.ds() : ds.value().name());
            }
            return proceedingJoinPoint.proceed();
        } finally {
            // 清空上下文信息
            if (ds != null) {
                DsContextHolder.reset();
            }
        }
    }

    private DsAno getDsAno(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        DsAno ds = method.getAnnotation(DsAno.class);
        if (ds == null) {
            // 获取类上的注解
            ds = (DsAno) proceedingJoinPoint.getSignature().getDeclaringType().getAnnotation(DsAno.class);
        }
        return ds;
    }
}
```

### 5.编程式数据源选择

前面也说到了，基于AOP方式的申明式数据源选择方案虽然简单，但是实用场景可能没有那么全，因此我们也设计了一种编程式的方式

```java
/**
 * 手动指定数据源的用法
 *
 * @author YiHui
 * @date 2023/4/30
 */
public class DsSelectExecutor {

    /**
     * 有返回结果
     *
     * @param ds
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T submit(DS ds, Supplier<T> supplier) {
        DsContextHolder.set(ds);
        try {
            return supplier.get();
        } finally {
            DsContextHolder.reset();
        }
    }

    /**
     * 无返回结果
     *
     * @param ds
     * @param call
     */
    public static void execute(DS ds, Runnable call) {
        DsContextHolder.set(ds);
        try {
            call.run();
        } finally {
            DsContextHolder.reset();
        }
    }
}
```

pass: 从上面实现也可以反推为啥MasterSlaveDsEnum这个枚举类为什么要继承DS接口

## 测试与小结

1. 测试

基于次上面的整个多数据源支持方案已经落地实现，接下来就是写几个case来验证一下了

直接再TestController中新加几个

直接访问第一个只读的接口，日志输出如下

## 优化

### 支持嵌套的数据源选择方案


在执行sql时，从上下文中获取当前选中的数据源,前面设计的上下文直接使用线程上线文保存

```java
public class DsContextHolder {
    /**
     * 使用继承的线程上下文，支持异步时选择传递
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<>();
}
```



我们定义一个DsNode节点来存储选中的数据源，区别于之前的String，它除了记录选中的数据源之外，还记录前一次选中的数据源

```java

public static class DsNode {
    DsNode pre;
    String ds;

    public DsNode(DsNode parent, String ds) {
        pre = parent;
        this.ds = ds;
    }
}
```



然后将线程上下文存储的替换为DsNode

```java

/**
 * 使用继承的线程上下文，支持异步时选择传递
 * 使用DsNode，支持链式的数据源切换，如最外层使用master数据源，内部某个方法使用slave数据源；但是请注意，对于事务的场景，不要交叉
 */
private static final ThreadLocal<DsNode> CONTEXT_HOLDER = new InheritableThreadLocal<>();
```



接下来就需要重点看一下写入当前选中的数据源的逻辑

- 每当有一个新的数据源选中时，新创建一个DsNode，并重新加入ThreadLocal
- 之前的上下文中的DsNode将作为新的DsNode的上一个节点

```java
public static void set(String dbType) {
    DsNode current = CONTEXT_HOLDER.get();
    CONTEXT_HOLDER.set(new DsNode(current, dbType));
}
```


移除上下文则不能像之前那么粗暴，而是要实现栈的弹出效果

- 如果无选中的数据源，直接返回
- 若之前选中的DsNode，存在上一个节点，则将上一个节点重新写入上下文
- 若之前选中的DsNode，不存在上一个节点，则清空上下文

```java
/**
 * 移除上下文
 */
public static void reset() {
    DsNode ds = CONTEXT_HOLDER.get();
    if (ds == null) {
        return;
    }

    if (ds.pre != null) {
        // 退出当前的数据源选择，切回去走上一次的数据源配置
        CONTEXT_HOLDER.set(ds.pre);
    } else {
        CONTEXT_HOLDER.remove();
    }
}
```

### Druid数据源支持


#### 1.DruidDataSource支持

```java
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.dynamic", name = "primary")
@EnableConfigurationProperties(DsProperties.class)
public class DataSourceConfig {
    @Bean
    @Primary
    public DataSource dataSource(DsProperties dsProperties) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(dsProperties.getDatasource().size());
        dsProperties.getDatasource().forEach((k, v) -> targetDataSources.put(k.toUpperCase(), v.initializeDataSourceBuilder().build()));
      // .... 省略细节
}
```

上面这段代码，初始化DataSource对象实际使用的是:

```java
DataSourceProperties.initializeDataSourceBuilder().build()
```



而它创建的数据源是默认的HiKariDataSource，但是当我们项目中实际使用的是DuridDataSource时，怎么办？

这个时候就需要我们数据源的实例化进行微调了; 首先添加一个Druid包的检测工具类（如果druid都没有引入，就不需要实例化DruidDataSource了）

```java
public class DruidCheckUtil {

    /**
     * 判断是否包含durid相关的数据包
     *
     * @return
     */
    public static boolean hasDuridPkg() {
        return ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", DataSourceConfig.class.getClassLoader());
    }
}
```


接下来就是将前面创建数据源的地方，单独的抽出一个方法，根据实际的情况来选择DataSource type


```java
@Bean
@Primary
public DataSource dataSource(DsProperties dsProperties) {
    Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(dsProperties.getDatasource().size());
    dsProperties.getDatasource().forEach((k, v) -> targetDataSources.put(k.toUpperCase(), initDataSource(k, v)));
    // ... 省略其他
}

public DataSource initDataSource(String prefix, DataSourceProperties properties) {
    if (!DruidCheckUtil.hasDuridPkg()) {
        log.info("实例化HikarDataSource: {}", prefix);
        return properties.initializeDataSourceBuilder().build();
    }

    if (properties.getType() == null || !properties.getType().isAssignableFrom(DruidDataSource.class)) {
        log.info("实例化HikarDataSource: {}", prefix);
        return properties.initializeDataSourceBuilder().build();
    }

    log.info("实例化DruidDataSource: {}", prefix);
    return Binder.get(environment).bindOrCreate("spring.dynamic.datasource." + prefix, DruidDataSource.class);
}
```



注意上面实例化DruidDataSource的方法，从envionment中读取配置前缀为spring.dynamic.datasource.master配置值来初始化DruidDataSource的成员变量，并创建一个对应的实例

上面这一块实现之后，我们的数据源就支持DruidDataSource了， 如再db配置信息中，添加一些druid的相关配置


```yaml
spring:
  dynamic: # 动态数据源
    primary: master # 这个表示默认的数据源
    datasource:
      master:
        # 数据库名，从配置 database.name 中获取
        url: jdbc:mysql://localhost:3306/${database.name}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        # 注意下面这个type,选择DruidDataSource时，请确保项目中添加了druid相关依赖
        type: com.alibaba.druid.pool.DruidDataSource
        #DruidDataSource自有属性
        filters: stat
        initialSize: 0
        minIdle: 1
        maxActive: 200
        maxWait: 10000
        time-between-eviction-runs-millis: 60000
        min-evictable-idle-time-millis: 200000
        testWhileIdle: true
        testOnBorrow: true
        validationQuery: select 1
```

然后我们的默认数据源master就是druidDataSource了；因为slave的没有指定type，所以还是默认的HikarDataSource，再项目启动之后，也可以从启动日志上看


![image.png](https://upload-images.jianshu.io/upload_images/4994935-89e9b24ace2ca1da.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 2.Mybatis拦截器适配

对于使用DruidDataSource的场景，我们还需要将前面介绍的SqlStateInterceptor做一点改造，以支持用户名的获取，关键点就再下面这里

![image.png](https://upload-images.jianshu.io/upload_images/4994935-9cce55b6962c73f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 3.Druid监控配置


既然使用到了druid数据源，那么不得不额外提一句的就是它配套的监控配置了，技术派中也给出了相关的设置


```java
/**
 * 当项目中引入了Druid相关包才创建
 * @return
 */
@Bean
@ConditionalOnExpression(value = "T(com.github.paicoding.forum.core.dal.DruidCheckUtil).hasDuridPkg()")
public ServletRegistrationBean<?> druidStatViewServlet() {
    //先配置管理后台的servLet，访问的入口为/druid/
    ServletRegistrationBean<?> servletRegistrationBean = new ServletRegistrationBean<>(
            new StatViewServlet(), "/druid/*");
    // IP白名单 (没有配置或者为空，则允许所有访问)
    servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
    // IP黑名单 (存在共同时，deny优先于allow)
    servletRegistrationBean.addInitParameter("deny", "");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "admin");
    servletRegistrationBean.addInitParameter("resetEnable", "false");
    log.info("开启druid数据源监控面板");
    return servletRegistrationBean;
}
```


至于项目中是否有Druid相关包，则可以直接通过控制paicoding-core项目的pom.xml中的依赖来决定


![image.png](https://upload-images.jianshu.io/upload_images/4994935-0c9bdcb62da0f8ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



仅以技术派这个工程进行说明：

- 当<scope>provided</scope> 被注释，则表示项目中包含druid相关包
- 当<scope>provided</scope> 未注释，则表示项目中不包含druid相关包

有没有druid的包，将直接决定访问druid监控页面到底长啥样，访问连接：[http://127.0.0.1:8080/druid/index.html](http://127.0.0.1:8080/druid/index.html)


## 小结

本文作为动态数据源切换的第三篇，再前面自定义的数据源切换方案上进行了补充和完善，支持嵌套的数据源选择，同时也支持了druidDataSource的配置

文中的所有技术，都可以直接再项目源码中获取，接下来给大家小结一下相关的知识点（敲黑板，下面的是重点，前面的不想看没关系，下面的东西请牢记）

- 使用栈的数据结构，结合上下文来实现嵌套的数据源切换方案
- InheritableThreadLocal支持主子线程数据共享
- 使用Binder来手动基于Spring的配置信息来实例并初始化bean
- 如何判断项目中是否引入了某个包的方式
- 条件判断是否需要创建某个bean @ConditionalOnExpression
- DruidDataSource的实例化方式
- Druid数据源的监控配置方式
