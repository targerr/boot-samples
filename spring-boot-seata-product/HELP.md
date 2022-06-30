# 整合seata

## 第一步：创建starter工程spring-boot-seata-product 和 spring-boot-seata-order xml文件

> 具体实现,查考代码示例

#### product xml文件

```xml

<dependencies>
    <!--jpa依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!--mysql驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!--seata依赖-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>

</dependencies>

<dependencyManagement>
<dependencies>
    <!--SpringCloudAlibaba依赖-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2.2.3.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>

```

## 第二步： 配置文件

```yaml
server:
  port: 8086

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_product
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: root123456

  cloud:
    alibaba:
      seata:
        tx-service-group: seata-product
  application:
    name: seata-product

seata:
  service:
    vgroup-mapping:
      seata-product: default
    grouplist:
      default: 127.0.0.1:8091
```

## 第三步：服务类

#### 1.1 entity

```java

@Data
@Entity
@Table(name = "t_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private Integer count;
}

```

#### 1.2 dao
```java
public interface ProductDao extends JpaRepository<ProductEntity, Long> {
}

```

#### 1.3 service
```java
@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void deduct(Long productId, Integer count) {
        log.info("开始扣库存. productId={}, count={}", productId, count);
        Optional<ProductEntity> byId = productDao.findById(productId);
        if (byId.isPresent()) {
            ProductEntity entity = byId.get();
            entity.setCount(entity.getCount() - count);
            productDao.save(entity);
        }
    }
}
```

#### 1.4 controller
```java
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/deduct")
    public Boolean deduct(@RequestParam Long productId,
                          @RequestParam Integer count) {
        productService.deduct(productId, count);
        return true;
    }
}

```
