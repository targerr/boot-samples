## 动态切换数据源

###  实现ThreadLocal

ThreadLocal作用
* 在一个线程共享的数据，不同线程互不影响
* ThreadLocal存入值时,会获取当前线程实例作为key，存入当前线程对象中的Map中。

AbstractRoutingDataSource
* 根据用户定义的规则选择当前的数据源
* 在执行查询之前，设置使用的数据源，实现动态路由的数据源，在每次数据库查询操作前执行它的抽象方法determineCurrentLookupKey()，决定使用哪个数据源。

### 代码实现

#### 1.实现ThreadLocal
```java
@Slf4j
public class DynamicDataSourceContextHolder{
    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER =new TransmittableThreadLocal<String>();

    /**
     * 设置数据源的变量
     */
    public static void setDataSourceType(String dsType)
    {
        log.info("切换到{}数据源", dsType);
        CONTEXT_HOLDER.set(dsType);
    }

    /**
     * 获得数据源的变量
     */
    public static String getDataSourceType()
    {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataSourceType()
    {
        CONTEXT_HOLDER.remove();
    }
}

```

#### 2. 实现AbstractRoutingDataSource
> 定义一个动态数据源类实现AbstractRoutingDataSource，通过determineCurrentLookupKey方法与上述实现的ThreadLocal类中的get方法进行关联，实现动态切换数据源。

```java
public class DynamicDataSource extends AbstractRoutingDataSource {
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
```

#### 3. 配置数据源
3.1 属性配置
```java

@Data
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidProperties
{
    private int initialSize;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int connectTimeout;

    private int socketTimeout;

    private int timeBetweenEvictionRunsMillis;

    private int minEvictableIdleTimeMillis;

    private int maxEvictableIdleTimeMillis;

    private String validationQuery;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    public DruidDataSource dataSource(DruidDataSource datasource)
    {
        /** 配置初始化大小、最小、最大 */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** 配置获取连接等待超时的时间 */
        datasource.setMaxWait(maxWait);
        
        /** 配置驱动连接超时时间，检测数据库建立连接的超时时间，单位是毫秒 */
        datasource.setConnectTimeout(connectTimeout);
        
        /** 配置网络超时时间，等待数据库操作完成的网络超时时间，单位是毫秒 */
        datasource.setSocketTimeout(socketTimeout);

        /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        datasource.setValidationQuery(validationQuery);
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);
        return datasource;
    }
}

```

3.2 配置文件
```java

@Configuration
public class DruidConfig {
    @Bean(name = "master")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }


    @Bean(name = "slave")
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }


    @Bean(name = "dynamicDataSource")
    @Primary
    public DataSource dataSource(
            @Autowired(required = false) @Qualifier("master") DataSource masterDataSource,
            @Autowired(required = false) @Qualifier("slave") DataSource slaveDataSource) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        // 正确地设置主库和从库数据源
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);

        // 检查是否启用了从库数据源
        if(Objects.nonNull(slaveDataSource)){
            targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
        }

        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

    /**
     * 去除监控页面底部的广告
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties)
    {
        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";
        // 创建filter进行过滤
        Filter filter = new Filter()
        {
            @Override
            public void init(javax.servlet.FilterConfig filterConfig) throws ServletException
            {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException
            {
                chain.doFilter(request, response);
                // 重置缓冲区，响应头不会被重置
                response.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }

            @Override
            public void destroy()
            {
            }
        };
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

    /**
     * 注入切面
     * @return
     */
    @Bean
    public DataSourceAspect dataSourceAspect(){
        return new DataSourceAspect();
    }
}

```

#### 4. 自定义多数据源切换注解

```java

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     * 切换数据源名称
     */
    public DataSourceType value() default DataSourceType.MASTER;
}

public enum DataSourceType {
    /**
     * 主库
     */
    MASTER,

    /**
     * 从库
     */
    SLAVE,
    /**
     * 灰度
     */
    GRAY;
}


```

#### 4. 切面
```java
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    @Pointcut("@annotation(com.laigeoffer.pmhub.base.datasource.annotation.DataSource)"
            + "|| @within(com.laigeoffer.pmhub.base.datasource.annotation.DataSource)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (Objects.nonNull(dataSource)) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String dbSource = request.getHeader("X-Datasource");
            if (StrUtil.isNotBlank(dbSource)) {
                DynamicDataSourceContextHolder.setDataSourceType(dbSource);
            }
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}


```

#### 5. META-INF spring
```
com.laigeoffer.pmhub.base.datasource.config.properties.DruidProperties
com.laigeoffer.pmhub.base.datasource.config.DruidConfig
```

参考

* [SpringBoot 实现动态切换数据源](https://mp.weixin.qq.com/s/abFgZDOu-N1pR4S0L4q3UQ)


