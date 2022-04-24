## spring-boot-mybatis

> 演示mybatis-plus,具体代码见 demo。

### pom

```xml

<dependencies>
    <!-- mysql数据库 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>
</dependencies>
```

### application

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springboot?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: root123456

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/*.xml

```

### 启动类

```java

@SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringBootMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisApplication.class, args);
    }

}


```

### service

```java

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}


```

### mapper

```java
public interface BlogMapper extends BaseMapper<Blog> {

}

```

### mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.BlogMapper">

</mapper>

```

### BlogController

```java

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService iBlogService;

    @GetMapping("/getDetail")
    public Blog getDetail(String id) {
        return iBlogService.getById(id);
    }

}


```

### sql日志打印
#### pom依赖
```xml
        <!-- SQL 分析打印 -->
        <dependency>
            <groupId>p6spy</groupId>
            <artifactId>p6spy</artifactId>
            <version>3.9.1</version>
        </dependency>
```
#### application配置
```yaml
spring:
  datasource:
    #        driver-class-name: com.mysql.cj.jdbc.Driver
    #        url: jdbc:mysql://localhost:3306/springboot?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    #        username: root
    #        password: root123456
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql://localhost:3306/springboot?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: root123456
```
#### 在resources下面创建spy.properties配置文件

### db

```java
SET NAMES utf8mb4;
        SET FOREIGN_KEY_CHECKS=0;

        ------------------------------
        --Table structure for blog
        ------------------------------
        DROP TABLE IF EXISTS `blog`;
        CREATE TABLE `blog` (
        `id` varchar(255)NOT NULL,
        `title` varchar(200)NOT NULL,
        `content` mediumtext NOT NULL,
        `create_date_time` datetime DEFAULT NULL,
        `modify_date_time` datetime DEFAULT NULL,
        PRIMARY KEY(`id`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;

        SET FOREIGN_KEY_CHECKS=1;

```