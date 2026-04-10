# Albedo DataScope Spring Boot Starter

基于 MyBatis Plus 的数据权限 Spring Boot Starter，通过 SQL 拦截器实现动态数据权限控制。

## 功能特性

- 🚀 **零侵入**：通过 MyBatis 拦截器自动修改 SQL，业务代码无需关注
- 🎯 **灵活配置**：支持部门级别、个人级别等多种数据权限类型
- 🔧 **易于集成**：标准的 Spring Boot Starter，开箱即用
- 📦 **独立模块**：可独立使用，不依赖 Albedo 其他模块
- 🌈 **多数据源**：支持多数据源环境

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.albedo.java</groupId>
    <artifactId>albedo-datascope-spring-boot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置文件

在 `application.yml` 中添加配置：

```yaml
albedo:
  datascope:
    enabled: true                    # 是否启用数据权限，默认启用
    default-scope-name: dept_id      # 默认权限字段名
    default-creator-name: created_by # 默认创建者字段名
    sql-log-enabled: false           # 是否打印 SQL 日志
```

### 3. 使用方法

#### 3.1 基本使用

在 Service 层创建 DataScope 对象并传递给 Mapper：

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> queryUsers() {
        // 创建数据权限对象
        DataScope dataScope = new DataScope();
        dataScope.setDeptIds(Sets.newHashSet(1L, 2L, 3L));

        // 传递给 Mapper
        return userMapper.selectList(dataScope);
    }
}
```

#### 3.2 Mapper 接口定义

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 第一个参数可以是查询条件，第二个参数是 DataScope
    List<User> selectList(@Param("queryDto") UserQueryDto queryDto,
                         @Param("dataScope") DataScope dataScope);
}
```

#### 3.3 使用工具类

```java
// 全部数据权限
DataScope allScope = DataScopeUtil.createAll();

// 部门数据权限
DataScope deptScope = DataScopeUtil.createDept(1L, 2L, 3L);

// 本人数据权限
DataScope selfScope = DataScopeUtil.createSelf(123L);

// 空权限（不过滤）
DataScope emptyScope = DataScopeUtil.createEmpty();
```

## 数据权限类型

| 类型 | 说明 | SQL 效果 |
|------|------|----------|
| ALL | 全部数据权限 | 不修改 WHERE 条件 |
| THIS_LEVEL_CHILDREN | 本级及子级 | `dept_id IN (1,2,3...)` |
| THIS_LEVEL | 本级 | `dept_id IN (1)` |
| SELF | 本人数据 | `created_by = 123` |
| CUSTOMIZE | 自定义 | `dept_id IN (自定义部门列表)` |

## 核心类说明

### DataScope

数据权限对象，包含以下属性：

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

### DataScopeInterceptor

MyBatis Plus 拦截器，在 SQL 执行前动态修改 WHERE 条件。

### DataScopeUtil

工具类，提供便捷的静态方法创建 DataScope 对象。

## 高级用法

### 1. 自定义权限字段

```java
// 按区域权限
DataScope dataScope = new DataScope();
dataScope.setScopeName("region_id");
dataScope.getDeptIds().addAll(Arrays.asList(1L, 2L, 3L));
```

### 2. 与 Spring Security 集成

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

    private DataScope buildDataScope(User user) {
        DataScope dataScope = new DataScope();
        dataScope.setUserId(user.getId());

        // 根据角色数据权限类型构建
        for (Role role : user.getRoles()) {
            switch (role.getDataScopeType()) {
                case ALL:
                    dataScope.setAll(true);
                    break;
                case THIS_LEVEL_CHILDREN:
                    dataScope.getDeptIds().addAll(
                        deptService.getDescendantIds(user.getDeptId())
                    );
                    break;
                case THIS_LEVEL:
                    dataScope.getDeptIds().add(user.getDeptId());
                    break;
                case SELF:
                    dataScope.setSelf(true);
                    break;
                case CUSTOMIZE:
                    dataScope.getDeptIds().addAll(
                        roleService.getCustomDeptIds(role.getId())
                    );
                    break;
            }
        }

        return dataScope;
    }
}
```

### 3. 禁用数据权限

```java
// 方式1：设置 isAll = true
DataScope dataScope = new DataScope();
dataScope.setAll(true);
userMapper.selectList(queryDto, dataScope);

// 方式2：传入 null
userMapper.selectList(queryDto, null);
```

## 配置说明

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| albedo.datascope.enabled | 是否启用数据权限 | true |
| albedo.datascope.default-scope-name | 默认权限字段名 | dept_id |
| albedo.datascope.default-creator-name | 默认创建者字段名 | created_by |
| albedo.datascope.sql-log-enabled | 是否打印 SQL 日志 | false |
| albedo.datascope.ignore-no-alias | 是否忽略无别名的 SQL | false |
| albedo.datascope.interceptor-order | 拦截器执行顺序 | -10 |

## 注意事项

### 1. 性能考虑

- 数据权限拦截器会对每个 SELECT 语句进行解析和修改
- 建议只在需要的查询中传递 DataScope 参数
- 对于不需要权限控制的查询，不要传递 DataScope 参数

### 2. SQL 解析限制

- 当前只支持 `PlainSelect` 类型的 SQL
- 不支持 UNION、复杂子查询等
- 如果遇到不支持的 SQL，会记录警告日志并跳过处理

### 3. 多租户兼容

- 如果项目使用了多租户，需要注意拦截器的执行顺序
- 建议将数据权限拦截器放在租户拦截器之后

### 4. 调试建议

```yaml
logging:
  level:
    com.albedo.java.plugins.datascope: DEBUG
```

## 常见问题

### Q1: 数据权限不生效？

检查项：
1. 配置文件中 `albedo.datascope.enabled` 是否为 `true`
2. Mapper 方法是否传递了 DataScope 参数
3. DataScope 对象的属性是否正确设置
4. 查看日志确认拦截器是否执行

### Q2: SQL 解析失败？

可能原因：
1. SQL 语法不支持（如 UNION、复杂子查询）
2. 表别名缺失
3. WHERE 条件格式错误

解决方案：
1. 简化 SQL 查询
2. 添加表别名
3. 检查 WHERE 语法

### Q3: 性能问题？

优化建议：
1. 只在需要的查询中传递 DataScope
2. 考虑使用缓存缓存部门树结构
3. 优化部门ID查询
4. 考虑使用数据库视图预处理权限

## 版本兼容性

- MyBatis Plus: 3.5.0+
- Spring Boot: 2.7.0+ / 3.x
- Java: 8+
- JSqlParser: 4.0+

## 许可证

Apache License 2.0

## 作者

Albedo Team

## 更多文档

- [Albedo 项目文档](https://github.com/somowhere/albedo)
- [数据权限模块详细指南](../../docs/DataScope-Module-Guide.md)
