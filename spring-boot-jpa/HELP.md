## spring-boot-jpa

> 演示jpa,具体代码见 demo。

### pom

```xml

<dependencies>
    <!--jap依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!--mysql驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>

</dependencies>
```

### service

```java
public interface BolgService {
    /**
     * 查询单个
     * @param id
     * @return
     */
    Blog findOneBlog(String id);
}

```

### repository

```java
public interface BlogRepository extends JpaRepository<Blog, String> {

}

```

### BlogController

```java

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BolgService bolgService;

    @GetMapping("/getDetail")
    public Blog getDetail(String id) {
        return bolgService.findOneBlog(id);
    }

}


```

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