### 动态数据源
要根据注解动态切换数据源，可以使用 `MyBatis-Plus` 和 `Dynamic Datasource` 库来实现。通过使用自定义注解并结合 `AOP` 切面技术，可以在执行方法时动态切换数据源。

### 步骤概述

1. **引入依赖**：确保在项目中引入了 `MyBatis-Plus` 和 `Dynamic Datasource` 相关的依赖。
2. **创建自定义数据源注解**：定义一个注解，用于标识需要切换的数据源。
3. **实现数据源切换逻辑**：使用 `AOP` 切面拦截带有该注解的方法，并切换数据源。
4. **配置多数据源**：在配置类中定义多个数据源，并使用 `DynamicDataSource` 来管理它们。
5. **示例代码**：提供一个基于以上步骤的完整示例。

### 1. 引入依赖

在 `pom.xml` 文件中引入必要的依赖：

``` xml
<dependencies>
    <!-- MyBatis-Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>

    <!-- Dynamic Datasource -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>3.5.0</version>
    </dependency>
    <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.6</version>
</dependency>
</dependencies>
```

### 2. 创建自定义数据源注解

```
package com.example.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default "master";  // 默认数据源为 master
}
```

### 3. 实现数据源切换逻辑

``` 
@Aspect
@Component
@Order(1)
public class DataSourceAspect {

    @Before("@annotation(dataSource)")
    public void switchDataSource(DataSource dataSource) {
        String dsId = dataSource.value();
        DynamicDataSourceContextHolder.push(dsId);
    }

    // 清理数据源
    @After("@annotation(dataSource)")
    public void clearDataSource(DataSource dataSource) {
        DynamicDataSourceContextHolder.clear();
    }
}
```

### 4. 配置多数据源
#### 4.1 druid数据源配置
```
@Configuration
public class DataSourceConfig {

    @Bean(name = "master")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "slave")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
              DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        dataSource.addDataSource("master", masterDataSource());
        dataSource.addDataSource("slave", masterDataSource());

        return dataSource;
    }
}
```

#### 4.2 配置多数据源
```java

package com.example.config;

import com.zaxxer.hikari.HikariDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name = "master")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "slave")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        dataSource.addDataSource("master", masterDataSource());
        dataSource.addDataSource("slave", masterDataSource());

        return dynamicDataSource;
    }
}

```

### 5.  配置文件
#### 5.1 Druid配置
```yaml
spring:
  datasource:
    master:
      url: jdbc:mysql://localhost:3306/master_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
      # Druid specific properties
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
    slave:
      url: jdbc:mysql://localhost:3306/slave_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
      # Druid specific properties
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20

```
#### 5.2 hikari配置 
```yaml
 spring:
   application:
     name: web-api
   datasource:
     master:
       jdbc-url: jdbc:mysql://172.16.200.212/anze_biz?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
       username: root
       password: 123456
       driver-class-name: com.mysql.cj.jdbc.Driver
       hikari:
         maximum-pool-size: 10
         minimum-idle: 5
         idle-timeout: 600000
         max-lifetime: 1800000
         connection-timeout: 30000

     slave:
       jdbc-url: jdbc:mysql://172.16.200.212/anze_base?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
       username: root
       password: 123456
       driver-class-name: com.mysql.cj.jdbc.Driver
       hikari:
         maximum-pool-size: 10
         minimum-idle: 5
         idle-timeout: 600000
         max-lifetime: 1800000
         connection-timeout: 30000


 mybatis-plus:
   mapper-locations:
     - classpath:/mapper/*.xml
 logging:
   level:
     com.example: debug
 server:
   port: 8080

 ```