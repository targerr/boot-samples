# @DataScopeAnno 注解使用指南

## 概述

`@DataScopeAnno` 是数据权限模块的核心注解，用于标识需要进行数据权限控制的 Mapper 方法或 Service 方法。通过 AOP 切面机制，自动从上下文中获取当前用户的数据权限信息并应用到查询中。

## 注解定义

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopeAnno {

    /**
     * 权限字段名称
     * 默认为 dept_id
     */
    String scopeName() default "dept_id";

    /**
     * 创建者字段名称
     * 默认为 created_by
     */
    String creatorName() default "created_by";

    /**
     * 表别名（可选）
     * 用于复杂查询中的表别名
     */
    String tableAlias() default "";

    /**
     * 是否启用数据权限
     * 默认启用
     */
    boolean enabled() default true;
}
```

## 属性说明

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `scopeName` | String | `"dept_id"` | 数据权限控制的字段名，通常是部门ID字段 |
| `creatorName` | String | `"created_by"` | 创建者字段名，用于个人数据权限 |
| `tableAlias` | String | `""` | 表别名，用于多表关联查询 |
| `enabled` | boolean | `true` | 是否启用数据权限控制 |

## 前置条件

使用 `@DataScopeAnno` 注解需要满足以下条件：

1. **引入 AOP 依赖**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

2. **启用 AOP 自动配置**
```yaml
albedo:
  datascope:
    aop-enabled: true  # 启用 AOP 切面功能
```

3. **实现 DataScopeProvider 接口**
如果你的项目使用 Spring Security，需要让 UserDetails 实现 `DataScopeAspect.DataScopeProvider` 接口。

## 使用方式

### 方式一：直接在 Mapper 方法上使用（推荐）

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @DataScopeAnno(scopeName = "dept_id")
    List<User> selectListWithDataScope(@Param("queryDto") UserQueryDto queryDto);
}
```

**说明**：
- AOP 切面会自动拦截带注解的方法
- 从 Spring Security 上下文中获取当前用户的数据权限
- 自动将数据权限条件应用到 SQL 中

**要求**：
- 需要实现 `DataScopeAspect.DataScopeProvider` 接口
- UserDetails 对象需要包含 `getDataScope()` 方法

### 方式二：在 Service 方法上使用

```java
@Service
public class UserServiceImpl implements UserService {

    @DataScopeAnno(scopeName = "dept_id", creatorName = "created_by")
    public List<User> queryUsers(UserQueryDto queryDto) {
        // AOP 切面会自动处理数据权限
        return userMapper.selectList(queryDto);
    }
}
```

**说明**：
- 适用于需要在 Service 层控制数据权限的场景
- Mapper 方法不需要传递 DataScope 参数

### 方式三：手动传递 DataScope 参数（不使用注解）

```java
@Service
public class UserServiceImpl implements UserService {

    public List<User> queryUsers(UserQueryDto queryDto) {
        // 手动创建 DataScope 对象
        DataScope dataScope = DataScopeUtil.createDept(1L, 2L, 3L);

        // 传递给 Mapper
        return userMapper.selectList(queryDto, dataScope);
    }
}
```

**说明**：
- 不需要 AOP 支持
- 不需要实现 DataScopeProvider 接口
- 灵活性更高，但需要手动管理 DataScope 对象

## 使用场景

### 场景一：部门数据权限

```java
@DataScopeAnno(scopeName = "dept_id")
List<User> selectByDept(@Param("deptId") Long deptId);
```

**生成的 SQL**：
```sql
SELECT * FROM sys_user WHERE dept_id IN (1, 2, 3)
```

### 场景二：个人数据权限

```java
@DataScopeAnno(creatorName = "created_by")
List<Task> selectMyTasks();
```

**生成的 SQL**：
```sql
SELECT * FROM sys_task WHERE created_by = 123
```

### 场景三：自定义权限字段

```java
@DataScopeAnno(scopeName = "region_id")
List<Order> selectByRegion();
```

**生成的 SQL**：
```sql
SELECT * FROM sys_order WHERE region_id IN (1, 2, 3)
```

### 场景四：多表关联查询

```java
@DataScopeAnno(scopeName = "dept_id", tableAlias = "u")
List<User> selectWithRoles();
```

**生成的 SQL**：
```sql
SELECT * FROM sys_user u
LEFT JOIN sys_user_role ur ON u.id = ur.user_id
WHERE u.dept_id IN (1, 2, 3)
```

### 场景五：禁用数据权限

```java
@DataScopeAnno(enabled = false)
List<User> selectAllUsers();
```

**说明**：临时禁用数据权限控制，查询全部数据。

## Spring Security 集成

### 实现 DataScopeProvider 接口

```java
@Getter
public class CustomUserDetails implements UserDetails, DataScopeAspect.DataScopeProvider {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final DataScope dataScope;

    public CustomUserDetails(User user, DataScope dataScope) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.dataScope = dataScope;
    }

    @Override
    public DataScope getDataScope() {
        return this.dataScope;
    }

    // 其他 UserDetails 方法实现...
}
```

### 在 UserDetailsService 中构建 DataScope

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptService deptService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 构建数据权限对象
        DataScope dataScope = buildDataScope(user);

        return new CustomUserDetails(user, dataScope);
    }

    private DataScope buildDataScope(User user) {
        DataScope dataScope = new DataScope();
        dataScope.setUserId(user.getId());

        // 获取用户的最大数据权限类型
        DataScopeType scopeType = getMaxDataScopeType(user);

        switch (scopeType) {
            case ALL:
                dataScope.setAll(true);
                break;
            case THIS_LEVEL_CHILDREN:
                Set<Long> deptIds = deptService.getDescendantIds(user.getDeptId());
                dataScope.getDeptIds().addAll(deptIds);
                break;
            case THIS_LEVEL:
                dataScope.getDeptIds().add(user.getDeptId());
                break;
            case SELF:
                dataScope.setSelf(true);
                break;
            case CUSTOMIZE:
                Set<Long> customDeptIds = getCustomDeptIds(user);
                dataScope.getDeptIds().addAll(customDeptIds);
                break;
            default:
                dataScope.setAll(true);
        }

        return dataScope;
    }
}
```

## 最佳实践

### 1. 注解放置位置

| 场景 | 推荐位置 | 原因 |
|------|----------|------|
| 简单 CRUD | Mapper 方法 | 更贴近 SQL，性能更好 |
| 复杂业务逻辑 | Service 方法 | 统一管理，便于维护 |
| 需要动态权限 | Service 方法 | 可以根据条件动态调整 |

### 2. 命名规范

```java
// 推荐：带 DataScope 后缀
List<User> selectListWithDataScope(...);
List<User> selectByDeptWithDataScope(...);

// 不推荐：没有后缀
List<User> selectList(...);
```

### 3. 配置管理

```yaml
# 开发环境：启用 SQL 日志
albedo:
  datascope:
    aop-enabled: true
    sql-log-enabled: true

# 生产环境：关闭 SQL 日志
albedo:
  datascope:
    aop-enabled: true
    sql-log-enabled: false
```

### 4. 异常处理

```java
@DataScopeAnno(scopeName = "dept_id")
List<User> selectUsers() {
    try {
        return userMapper.selectList();
    } catch (Exception e) {
        log.error("数据权限查询失败", e);
        // 降级处理：返回空列表或抛出业务异常
        return Collections.emptyList();
    }
}
```

## 常见问题

### Q1: @DataScopeAnno 不生效？

**检查项**：
1. 是否引入了 `spring-boot-starter-aop` 依赖
2. 配置文件中 `albedo.datascope.aop-enabled` 是否为 `true`
3. UserDetails 是否实现了 `DataScopeProvider` 接口
4. DataScope 对象是否正确返回

```yaml
# 检查配置
albedo:
  datascope:
    aop-enabled: true  # 必须为 true
```

### Q2: 如何调试数据权限？

```yaml
# 启用调试日志
logging:
  level:
    com.albedo.java.plugins.datascope.aspect: DEBUG
    com.albedo.java.plugins.datascope.interceptor: DEBUG
```

### Q3: 能否同时使用注解和参数？

```java
// 不推荐：注解和参数同时使用
@DataScopeAnno(scopeName = "dept_id")
List<User> selectList(@Param("dataScope") DataScope dataScope);

// 推荐：只使用一种方式
// 方式1：仅注解
@DataScopeAnno(scopeName = "dept_id")
List<User> selectList();

// 方式2：仅参数
List<User> selectList(@Param("dataScope") DataScope dataScope);
```

### Q4: 如何临时禁用数据权限？

```java
// 方式1：设置 enabled = false
@DataScopeAnno(enabled = false)
List<User> selectAll();

// 方式2：不使用注解
List<User> selectAll();

// 方式3：DataScope 设置为全部权限
DataScope dataScope = DataScopeUtil.createAll();
```

### Q5: 表别名什么时候需要设置？

```java
// 单表查询：不需要设置表别名
@DataScopeAnno(scopeName = "dept_id")
List<User> selectUsers();

// 多表查询：需要设置表别名
@DataScopeAnno(scopeName = "dept_id", tableAlias = "u")
List<User> selectWithRoles();
```

## 完整示例

### 实体类

```java
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private Long deptId;      // 部门ID（数据权限字段）
    private Long createdBy;   // 创建者ID（个人权限字段）
    private LocalDateTime createdDate;
}
```

### Mapper 接口

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @DataScopeAnno(scopeName = "dept_id")
    List<User> selectListWithDataScope(@Param("queryDto") UserQueryDto queryDto);
}
```

### Service 实现

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 使用 AOP 注解方式
    @DataScopeAnno(scopeName = "dept_id")
    public List<User> queryUsers(UserQueryDto queryDto) {
        return userMapper.selectListWithDataScope(queryDto);
    }

    // 或手动传递 DataScope
    public List<User> queryUsersManually(UserQueryDto queryDto) {
        DataScope dataScope = getCurrentUserDataScope();
        return userMapper.selectListWithDataScope(queryDto, dataScope);
    }

    private DataScope getCurrentUserDataScope() {
        // 从 Security 上下文获取
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof DataScopeAspect.DataScopeProvider) {
            return ((DataScopeAspect.DataScopeProvider) auth.getPrincipal()).getDataScope();
        }
        return DataScopeUtil.createEmpty();
    }
}
```

### 配置文件

```yaml
albedo:
  datascope:
    enabled: true
    aop-enabled: true           # 启用 AOP 切面
    default-scope-name: dept_id
    default-creator-name: created_by
    sql-log-enabled: true       # 开发环境启用 SQL 日志
```

## 版本兼容性

- MyBatis Plus: 3.5.0+
- Spring Boot: 2.7.0+ / 3.x
- Spring Security: 5.0+ (可选)
- AOP: 需要引入 `spring-boot-starter-aop`

## 相关文档

- [README.md](README.md) - 项目概述
- [USAGE.md](USAGE.md) - 完整使用指南
- [DataScope 开发指南](DEVELOPMENT.md) - 开发相关说明

## 许可证

Apache License 2.0
