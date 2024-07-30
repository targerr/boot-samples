### 1. Maven依赖

首先，您需要添加MyBatis-Plus和数据库驱动的Maven依赖到您的`pom.xml`文件中。

```
xml
Copy code
<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.3.3</version>
</dependency>

<!-- 数据库驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.26</version>
</dependency>
```

### 2. 配置多数据源

在`application.properties`或`application.yml`文件中配置多数据源。

```
properties
Copy code
# 数据源1
spring.datasource.first.url=jdbc:mysql://localhost:3306/db_first
spring.datasource.first.username=root
spring.datasource.first.password=root

# 数据源2
spring.datasource.second.url=jdbc:mysql://localhost:3306/db_second
spring.datasource.second.username=root
spring.datasource.second.password=root
```

### 3. 数据源配置类

创建两个数据源的配置类，并使用`@ConfigurationProperties`注解来映射属性。

```
java
Copy code
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.first")
public class FirstDataSourceConfig {
    private String url;
    private String username;
    private String password;
    // getters and setters
}

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.second")
public class SecondDataSourceConfig {
    private String url;
    private String username;
    private String password;
    // getters and setters
}
```

### 4. 数据源、SessionFactory和TransactionManager配置

```
java
Copy code
@Configuration
public class DataSourceConfig {

    @Bean(name = "firstDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "firstSqlSessionFactory")
    public SqlSessionFactory firstSqlSessionFactory(@Qualifier("firstDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "secondSqlSessionFactory")
    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "firstTransactionManager")
    public DataSourceTransactionManager firstTransactionManager(@Qualifier("firstDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "secondTransactionManager")
    public DataSourceTransactionManager secondTransactionManager(@Qualifier("secondDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

### 5. MyBatis-Plus配置

创建MyBatis-Plus的配置类，并设置Mapper扫描路径。

```
java
Copy code
@Configuration
@MapperScan(basePackages = "com.example.mapper", sqlSessionTemplateRef = "firstSqlSessionTemplate")
public class MybatisPlusConfig {

    @Bean(name = "firstSqlSessionTemplate")
    public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("firstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "secondSqlSessionTemplate")
    public SqlSessionTemplate secondSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

### 6. Mapper接口

定义两个Mapper接口，并使用`@Mapper`注解指定使用哪个数据源。

```
java
Copy code
@Mapper
public interface FirstMapper extends BaseMapper<YourEntity> {
    // Your methods
}

@Mapper
public interface SecondMapper extends BaseMapper<YourEntity> {
    // Your methods
}
```

这样，您就配置好了MyBatis-Plus与多数据源的环境。在需要使用不同数据源的地方，只需指定对应的Mapper或SqlSessionTemplate即可。



### 1. 注解定义

首先，定义两个注解来标记不同的数据源。

```
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource1 {
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource2 {
}
```

### 2. 数据源切换切面

创建一个切面类，在切面中根据注解的类型动态切换数据源。

```
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.example.datasource.DataSource1)")
    public void dataSource1Pointcut() {
    }

    @Pointcut("@annotation(com.example.datasource.DataSource2)")
    public void dataSource2Pointcut() {
    }

    @Around("dataSource1Pointcut()")
    public Object dataSource1Around(ProceedingJoinPoint joinPoint) throws Throwable {
        DataSourceContextHolder.setDataSource(DataSourceType.DATASOURCE1);
        try {
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSource();
        }
    }

    @Around("dataSource2Pointcut()")
    public Object dataSource2Around(ProceedingJoinPoint joinPoint) throws Throwable {
        DataSourceContextHolder.setDataSource(DataSourceType.DATASOURCE2);
        try {
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSource();
        }
    }
}
```

在上述代码中，`DataSourceContextHolder`是一个简单的上下文持有者类，用于存储当前线程使用的数据源类型。

```
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSource(DataSourceType dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    public static DataSourceType getDataSource() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}

public enum DataSourceType {
    DATASOURCE1,
    DATASOURCE2
}
```

### 3. 配置MyBatis-Plus

在MyBatis-Plus的配置类中，通过`DynamicDataSource`动态决定使用哪个数据源。

```
@Configuration
public class MybatisPlusConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("firstDataSource") DataSource firstDataSource,
                                               @Qualifier("secondDataSource") DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.DATASOURCE1, firstDataSource);
        targetDataSources.put(DataSourceType.DATASOURCE2, secondDataSource);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(firstDataSource);  // 默认数据源

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

### 4. 使用注解切换数据源

在您的Service或Mapper接口上使用`@DataSource1`或`@DataSource2`注解来指定数据源。

```
java
Copy code
@Service
public class YourService {

    @Autowired
    private YourMapper yourMapper;

    @DataSource1
    public List<YourEntity> getDataFromDataSource1() {
        return yourMapper.selectList(null);
    }

    @DataSource2
    public List<YourEntity> getDataFromDataSource2() {
        return yourMapper.selectList(null);
    }
}
```

通过这种方式，您可以根据方法或类上的注解动态切换数据源，实现多数据源的需求。