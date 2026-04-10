# 数据权限 Spring Boot Starter 模块开发总结

## 1. 模块概述

成功创建了独立的 **albedo-datascope-spring-boot-starter** 模块，这是一个基于 MyBatis Plus 的数据权限控制 Spring Boot Starter，可以独立使用，不依赖 Albedo 项目的其他模块。

## 2. 模块结构

```
albedo-datascope-spring-boot-starter/
├── pom.xml                                 # Maven 配置文件
├── README.md                               # 模块说明文档
├── USAGE.md                                # 详细使用指南
├── DEVELOPMENT.md                          # 开发总结文档（本文件）
└── src/main/
    ├── java/com/albedo/java/plugins/datascope/
    │   ├── annotation/
    │   │   └── DataScopeAnno.java         # 数据权限注解
    │   ├── aspect/
    │   │   └── DataScopeAspect.java       # AOP 切面支持
    │   ├── config/
    │   │   ├── DataScopeAutoConfiguration.java       # 主自动配置类
    │   │   ├── DataScopeAspectAutoConfiguration.java # AOP 自动配置类
    │   │   └── DataScopeProperties.java    # 配置属性类
    │   ├── enums/
    │   │   └── DataScopeType.java         # 数据权限类型枚举
    │   ├── interceptor/
    │   │   └── DataScopeInterceptor.java   # 核心 SQL 拦截器
    │   ├── model/
    │   │   └── DataScope.java             # 数据权限对象
    │   └── util/
    │       └── DataScopeUtil.java         # 工具类
    └── resources/META-INF/
        ├── spring.factories                               # Spring Boot 2.x 自动配置
        └── spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports  # Spring Boot 3.x 自动配置
```

## 3. 核心功能

### 3.1 数据权限拦截器

**DataScopeInterceptor** 是模块的核心组件，它实现了 MyBatis Plus 的 `InnerInterceptor` 接口：

- 在 SQL 执行前拦截 SELECT 语句
- 从方法参数中查找 `DataScope` 对象
- 使用 JSqlParser 解析并动态修改 SQL WHERE 条件
- 支持部门级别（dept_id）和创建者级别（created_by）的数据过滤

### 3.2 数据权限对象

**DataScope** 是数据权限的载体对象：

```java
public class DataScope {
    private String scopeName = "dept_id";      // 权限字段名
    private String creatorName = "created_by"; // 创建者字段名
    private Set<Long> deptIds;                 // 部门ID集合
    private boolean isAll;                     // 是否全部数据
    private boolean isSelf;                    // 是否本人数据
    private Long userId;                       // 用户ID
    private String tableAlias;                 // 表别名
}
```

### 3.3 数据权限类型

**DataScopeType** 枚举定义了多种权限类型：

| 类型 | 说明 | SQL 效果 |
|------|------|----------|
| ALL | 全部数据权限 | 不修改 WHERE 条件 |
| THIS_LEVEL_CHILDREN | 本级及子级 | `dept_id IN (1,2,3...)` |
| THIS_LEVEL | 本级 | `dept_id IN (1)` |
| SELF | 本人数据 | `created_by = 123` |
| CUSTOMIZE | 自定义 | `dept_id IN (自定义列表)` |

### 3.4 工具类

**DataScopeUtil** 提供了便捷的静态方法：

- `createAll()` - 创建全部数据权限
- `createDept(Set<Long>)` - 创建部门数据权限
- `createDept(Long...)` - 创建部门数据权限（可变参数）
- `createSelf(Long)` - 创建本人数据权限
- `createEmpty()` - 创建空权限（不过滤）

### 3.5 AOP 支持（可选）

**DataScopeAspect** 提供了 AOP 切面支持：

- 通过 `@DataScopeAnno` 注解自动处理数据权限
- 从 Spring Security 上下文中自动获取当前用户的数据权限
- 需要引入 `spring-boot-starter-aop` 依赖并启用 AOP 配置

## 4. 自动配置

### 4.1 主自动配置类

**DataScopeAutoConfiguration** 负责配置核心拦截器：

```java
@AutoConfiguration
@EnableConfigurationProperties(DataScopeProperties.class)
@ConditionalOnClass(MybatisPlusInterceptor.class)
@ConditionalOnProperty(prefix = "albedo.datascope", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataScopeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(DataScopeProperties properties) {
        // 创建并配置拦截器
    }
}
```

### 4.2 AOP 自动配置类

**DataScopeAspectAutoConfiguration** 负责配置 AOP 切面：

```java
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
@ConditionalOnProperty(prefix = "albedo.datascope", name = "aop-enabled", havingValue = "true", matchIfMissing = false)
public class DataScopeAspectAutoConfiguration {
    @Bean
    public DataScopeAspect dataScopeAspect() {
        return new DataScopeAspect();
    }
}
```

### 4.3 配置属性

**DataScopeProperties** 定义了所有可配置项：

```yaml
albedo:
  datascope:
    enabled: true                    # 是否启用数据权限
    default-scope-name: dept_id      # 默认权限字段名
    default-creator-name: created_by # 默认创建者字段名
    sql-log-enabled: false           # 是否打印 SQL 日志
    ignore-no-alias: false           # 是否忽略无别名的 SQL
    interceptor-order: -10           # 拦截器执行顺序
    aop-enabled: false               # 是否启用 AOP 支持
```

## 5. Spring Boot 自动装配

### 5.1 Spring Boot 2.x 支持

通过 `META-INF/spring.factories` 文件：

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.albedo.java.plugins.datascope.config.DataScopeAutoConfiguration,\
com.albedo.java.plugins.datascope.config.DataScopeAspectAutoConfiguration
```

### 5.2 Spring Boot 3.x 支持

通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件：

```
com.albedo.java.plugins.datascope.config.DataScopeAutoConfiguration
com.albedo.java.plugins.datascope.config.DataScopeAspectAutoConfiguration
```

## 6. 依赖管理

模块依赖以下库：

```xml
<!-- Spring Boot Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>

<!-- MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>

<!-- JSqlParser (SQL解析) -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
</dependency>

<!-- Hutool (工具类) -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-core</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- Guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
</dependency>

<!-- Spring AOP (可选，用于注解支持) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <optional>true</optional>
</dependency>
```

## 7. 使用方式

### 7.1 基本使用

```java
// Service 层
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> queryUsers() {
        // 创建数据权限对象
        DataScope dataScope = DataScopeUtil.createDept(1L, 2L, 3L);
        // 传递给 Mapper
        return userMapper.selectList(dataScope);
    }
}
```

### 7.2 与 Spring Security 集成

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByUsername(username);
        // 构建数据权限对象
        DataScope dataScope = buildDataScope(user);
        // 创建带有数据权限的 UserDetails
        return new CustomUserDetails(user, dataScope);
    }
}
```

## 8. 技术亮点

1. **零侵入性**：通过 MyBatis 拦截器自动修改 SQL，业务代码无需关注
2. **灵活配置**：支持多种权限类型组合，可自定义权限字段
3. **易于集成**：标准的 Spring Boot Starter，支持自动配置
4. **独立使用**：不依赖 Albedo 其他模块，可独立引入
5. **版本兼容**：同时支持 Spring Boot 2.x 和 3.x
6. **AOP 增强**：提供可选的 AOP 切面支持，自动处理数据权限

## 9. 注意事项

### 9.1 性能考虑

- 拦截器会对每个 SELECT 语句进行解析和修改
- 建议只在需要的查询中传递 DataScope 参数
- 对于不需要权限控制的查询，不要传递 DataScope 参数

### 9.2 SQL 限制

- 当前只支持 `PlainSelect` 类型的 SQL
- 不支持 UNION、复杂子查询等
- 如果遇到不支持的 SQL，会记录警告日志并跳过处理

### 9.3 多租户兼容

- 如果项目使用了多租户，需要注意拦截器的执行顺序
- 建议将数据权限拦截器放在租户拦截器之后

## 10. 后续优化建议

1. **支持更复杂的 SQL**：增强 SQL 解析能力，支持 UNION、子查询等
2. **缓存优化**：缓存 DataScope 对象，减少重复构建
3. **性能监控**：添加性能监控指标，统计拦截器执行时间
4. **插件化**：支持自定义数据权限策略
5. **文档完善**：添加更多使用示例和最佳实践

## 11. 总结

成功将 Albedo 项目中的数据权限模块提取为一个独立的 Spring Boot Starter，具有以下特点：

- ✅ 完全独立，可单独使用
- ✅ 标准 Spring Boot Starter 规范
- ✅ 支持 Spring Boot 2.x 和 3.x
- ✅ 提供完整的文档和使用示例
- ✅ 代码结构清晰，易于扩展

模块已创建完成，可以编译并发布到 Maven 仓库供其他项目使用。
