### 数据库文档生成

### 一. 添加依赖

```xml

<dependencies>
    <!--mysql驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- screw 生成数据库文档 -->
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>2.3.30</version>
    </dependency>
    <dependency>
        <groupId>cn.smallbun.screw</groupId>
        <artifactId>screw-core</artifactId>
        <version>1.0.3</version>
    </dependency>
</dependencies>
```

### 二. 配置

```yaml

spring:
  application:
    name: commerce-authority-center
  datasource:
    # 数据源
    url: jdbc:mysql://127.0.0.1:3306/commerce?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: root123456
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    # 连接池
    hikari:
      maximum-pool-size: 8
      minimum-idle: 4
      idle-timeout: 30000
      connection-timeout: 30000
      max-lifetime: 45000
      auto-commit: true
      pool-name: ImoocEcommerceHikariCP
```

### 三. 测试

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class DBDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDBDoc() {

        DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);

        // 生成文件配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir("/Users/wanggaoshuai/develop/IdeaProjects/gitee/boot-samples/spring-cloud-commerce" +
                        "/commerce-authority-center/src/test/java/com/example")
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker).build();

        // 生成文档配置, 包含自定义版本号、描述等等
        // 数据库名_description_version.html
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .description("e-commerce-springcloud")
                .dataSource(dataSourceMysql)
                .engineConfig(engineConfig)
                .produceConfig(getProduceConfig())
                .build();

        // 执行生成
        new DocumentationExecute(config).execute();
    }

    /**
     * <h2>配置想要生成的数据表和想要忽略的数据表</h2>
     * */
    private ProcessConfig getProduceConfig() {

        // 想要忽略的数据表
        List<String> ignoreTableName = Collections.singletonList("undo_log");
        // 忽略表前缀, 忽略 a、b 开头的数据库表
        List<String> ignorePrefix = Arrays.asList("a", "b");
        // 忽略表后缀
        List<String> ignoreSuffix = Arrays.asList("_test", "_Test");

        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(Collections.emptyList())
                // 根据表前缀生成
                .designatedTablePrefix(Collections.emptyList())
                // 根据表后缀生成
                .designatedTableSuffix(Collections.emptyList())
                // 忽略表
                .ignoreTableName(ignoreTableName)
                // 按照前缀忽略
                .ignoreTablePrefix(ignorePrefix)
                // 按照后缀忽略
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }
}

```
