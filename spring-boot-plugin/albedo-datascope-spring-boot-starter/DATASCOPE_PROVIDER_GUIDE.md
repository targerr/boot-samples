# DataScopeProvider 使用指南

## 概述

`albedo-datascope-spring-boot-starter` 已完全解耦 Spring Security，支持任意认证方式（JWT、Session、自定义等）。

## 快速开始

### 1. 实现 DataScopeProvider 接口

创建一个类实现 `DataScopeProvider` 接口，并注册为 Spring Bean：

```java
package com.example.datascope;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.provider.DataScopeProvider;
import com.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MyDataScopeProvider implements DataScopeProvider {

    @Override
    public DataScope getDataScope() {
        // 从任意地方获取当前用户
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }

        // 构建数据权限对象
        DataScope dataScope = new DataScope();

        // 方式1：部门权限
        dataScope.getDeptIds().addAll(currentUser.getDeptIds());

        // 方式2：个人权限
        // dataScope.setSelf(true);
        // dataScope.setUserId(currentUser.getId());

        // 方式3：全部权限
        // dataScope.setAll(true);

        return dataScope;
    }

    private User getCurrentUser() {
        // TODO: 从你的认证方式中获取当前用户
        // 例如：
        // - JWT: 从 ThreadLocal 获取
        // - Session: 从 HttpSession 获取
        // - 自定义: 从你的上下文获取

        return UserContext.getCurrentUser();
    }
}
```

### 2. 启用 AOP 支持

在 `application.yml` 中配置：

```yaml
albedo:
  datascope:
    enabled: true        # 启用数据权限（默认开启）
    aop-enabled: true    # 启用 AOP 支持（默认关闭）
```

### 3. 添加 AOP 依赖

在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 4. 使用 @DataScopeAnno 注解

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // AOP 会自动调用 DataScopeProvider 获取数据权限
    @DataScopeAnno(scopeName = "dept_id")
    public List<User> listUsers(UserQueryDto queryDto) {
        return userMapper.selectList(queryDto);
    }
}
```

## 不同认证方式示例

### JWT 认证

```java
@Component
public class JwtDataScopeProvider implements DataScopeProvider {

    @Override
    public DataScope getDataScope() {
        // 从 JWT ThreadLocal 中获取用户信息
        JwtUser jwtUser = JwtThreadLocal.get();
        if (jwtUser == null) {
            return null;
        }

        DataScope dataScope = new DataScope();
        dataScope.getDeptIds().addAll(jwtUser.getDeptIds());
        return dataScope;
    }
}
```

### Session 认证

```java
@Component
public class SessionDataScopeProvider implements DataScopeProvider {

    @Autowired
    private HttpSession session;

    @Override
    public DataScope getDataScope() {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return null;
        }

        DataScope dataScope = new DataScope();
        dataScope.getDeptIds().addAll(user.getDeptIds());
        return dataScope;
    }
}
```

### 自定义认证上下文

```java
@Component
public class CustomDataScopeProvider implements DataScopeProvider {

    @Override
    public DataScope getDataScope() {
        // 从自定义的用户上下文中获取
        return UserContext.getDataScope();
    }
}

// 用户上下文工具类
public class UserContext {
    private static final ThreadLocal<DataScope> CONTEXT = new ThreadLocal<>();

    public static void setDataScope(DataScope dataScope) {
        CONTEXT.set(dataScope);
    }

    public static DataScope getDataScope() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
```

### 配合权限系统

```java
@Component
public class RoleDataScopeProvider implements DataScopeProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public DataScope getDataScope() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }

        // 获取用户的最大数据权限类型
        DataScopeType scopeType = roleService.getMaxDataScopeType(userId);

        DataScope dataScope = new DataScope();

        switch (scopeType) {
            case ALL:
                dataScope.setAll(true);
                break;
            case THIS_LEVEL_CHILDREN:
                // 本部门及子部门
                dataScope.getDeptIds().addAll(
                    deptService.getDescendantIds(user.getDeptId())
                );
                break;
            case THIS_LEVEL:
                // 仅本部门
                dataScope.getDeptIds().add(user.getDeptId());
                break;
            case SELF:
                // 仅本人数据
                dataScope.setSelf(true);
                dataScope.setUserId(userId);
                break;
            case CUSTOMIZE:
                // 自定义部门权限
                dataScope.getDeptIds().addAll(
                    roleService.getCustomDeptIds(userId)
                );
                break;
            default:
                dataScope.setAll(true);
        }

        return dataScope;
    }
}
```

## 工作原理

```
┌─────────────────────────────────────────────────────────────┐
│  1. 用户请求 → Controller → Service (@DataScopeAnno)        │
│  ↓                                                          │
│  2. AOP 拦截器拦截方法                                       │
│  ↓                                                          │
│  3. DataScopeAspect.getCurrentUserDataScope()              │
│  ↓                                                          │
│  4. 从 Spring 容器获取 DataScopeProvider Bean               │
│  ↓                                                          │
│  5. 调用 provider.getDataScope() 获取数据权限               │
│  ↓                                                          │
│  6. 设置到 ThreadLocal                                      │
│  ↓                                                          │
│  7. 执行业务方法 → Mapper 查询                             │
│  ↓                                                          │
│  8. DataScopeInterceptor 从 ThreadLocal 获取并修改 SQL      │
└─────────────────────────────────────────────────────────────┘
```

## 注意事项

1. **线程安全**：DataScope 使用 ThreadLocal 存储，每个请求线程独立
2. **内存泄漏**：AOP 会自动清除 ThreadLocal，无需手动清理
3. **性能**：DataScopeProvider 只在带 @DataScopeAnno 注解的方法中调用
4. **可选**：如果不实现 DataScopeProvider，则不进行数据权限过滤

## 向后兼容

原有的 Spring Security 集成方式仍然可用：

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByUsername(username);
        DataScope dataScope = buildDataScope(user);

        // 让 UserDetails 同时实现 DataScopeProvider
        return new CustomUserDetails(user, dataScope);
    }
}

// UserDetails 实现
@Getter
public class CustomUserDetails implements UserDetails, DataScopeProvider {
    private final DataScope dataScope;
    // ... 其他方法
}
```

## 更多文档

- [README.md](README.md) - 项目概述
- [USAGE.md](USAGE.md) - 完整使用指南
- [DATASCOPE_ANNOTATION_GUIDE.md](DATASCOPE_ANNOTATION_GUIDE.md) - 注解使用指南
