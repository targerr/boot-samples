## spring-boot-mybatis

> 演示mybatis,具体代码见 demo。

### pom
```java
       <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```
### 启动类

```java
@SpringBootApplication
@MapperScan("com.example.dao")
public class SpringBootMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisApplication.class, args);
    }

}


```

### service


```java
public interface BlogService {
    int add(Blog blog);
    int update(Blog blog);
    int deleteBysno(String id);
    Blog queryStudentBySno(String id);
}

```

### dao
```java
public interface BlogMapper {
    int deleteByPrimaryKey(String id);
}    
```
### mapper
```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.dao.BlogMapper">
    <delete id="deleteByPrimaryKey" parameterType="java.lang.String">
        delete
        from blog
        where id = #{id,jdbcType=VARCHAR}
    </delete>
</mapper>
```

### BlogController

```java
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @RequestMapping( value = "/selectOne", method = RequestMethod.GET)
    public Blog queryStudentBySno(String id) {
        return blogService.queryStudentBySno(id);
    }

}

```
### db
```java
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` varchar(255) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `modify_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

```