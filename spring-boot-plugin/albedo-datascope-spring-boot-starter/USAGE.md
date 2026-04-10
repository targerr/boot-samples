# 数据权限模块独立使用示例

## 概述

本指南展示如何将 `albedo-datascope-spring-boot-starter` 作为一个独立的 Spring Boot Starter 在其他项目中使用。

## 1. 依赖引入

### Maven

```xml
<dependency>
    <groupId>com.albedo.java</groupId>
    <artifactId>albedo-datascope-spring-boot-starter</artifactId>
    <version>3.3.10-SNAPSHOT</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.albedo.java:albedo-datascope-spring-boot-starter:3.3.10-SNAPSHOT'
```

## 2. 完整使用示例

### 2.1 项目结构

```
your-project/
├── src/main/java/
│   └── com/example/
│       ├── controller/
│       │   └── UserController.java
│       ├── service/
│       │   ├── UserService.java
│       │   └── impl/
│       │       └── UserServiceImpl.java
│       ├── mapper/
│       │   └── UserMapper.java
│       ├── entity/
│       │   └── User.java
│       └── config/
│           └── SecurityConfig.java
└── resources/
    └── application.yml
```

### 2.2 实体类

```java
package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Long deptId;

    private Long createdBy;

    private LocalDateTime createdDate;

    private Integer delFlag;

    private String tenantCode;
}
```

### 2.3 Mapper 接口

```java
package com.example.mapper;

import com.albedo.java.plugins.datascope.annotation.DataScopeAnno;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户列表（带数据权限）
     *
     * @param queryDto 查询条件
     * @param dataScope 数据权限对象
     * @return 用户列表
     */
    @DataScopeAnno(scopeName = "dept_id")
    List<User> selectListWithDataScope(@Param("queryDto") UserQueryDto queryDto,
                                       @Param("dataScope") DataScope dataScope);

    /**
     * 查询用户列表（不带数据权限）
     *
     * @param queryDto 查询条件
     * @return 用户列表
     */
    List<User> selectListWithoutDataScope(@Param("queryDto") UserQueryDto queryDto);
}
```

### 2.4 Mapper XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.UserMapper">

    <select id="selectListWithDataScope" resultType="com.example.entity.User">
        SELECT *
        FROM sys_user
        <where>
            <if test="queryDto.username != null and queryDto.username != ''">
                AND username LIKE CONCAT('%', #{queryDto.username}, '%')
            </if>
            <if test="queryDto.deptId != null">
                AND dept_id = #{queryDto.deptId}
            </if>
            <if test="queryDto.delFlag != null">
                AND del_flag = #{queryDto.delFlag}
            </if>
            <!-- DataScope 拦截器会自动添加数据权限条件 -->
        </where>
        ORDER BY id DESC
    </select>

    <select id="selectListWithoutDataScope" resultType="com.example.entity.User">
        SELECT *
        FROM sys_user
        <where>
            <if test="queryDto.username != null and queryDto.username != ''">
                AND username LIKE CONCAT('%', #{queryDto.username}, '%')
            </if>
        </where>
        ORDER BY id DESC
    </select>

</mapper>
```

### 2.5 Service 层

```java
package com.example.service;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryUsers(UserQueryDto queryDto) {
        // 从当前登录用户获取数据权限
        DataScope dataScope = getCurrentUserDataScope();

        // 传递给 Mapper
        return userMapper.selectListWithDataScope(queryDto, dataScope);
    }

    @Override
    public List<User> queryAllUsers(UserQueryDto queryDto) {
        // 不进行数据权限过滤
        return userMapper.selectListWithoutDataScope(queryDto);
    }

    /**
     * 获取当前用户的数据权限
     * 这里需要根据你的实际业务逻辑实现
     */
    private DataScope getCurrentUserDataScope() {
        // 方式1：从 Spring Security 上下文获取
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getDataScope();
        }

        // 方式2：从线程局部变量获取
        // return UserContextHolder.getDataScope();

        // 方式3：手动构建
        return DataScopeUtil.createDept(1L, 2L, 3L);
    }
}
```

### 2.6 Spring Security 集成

```java
package com.example.config;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptService deptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 构建数据权限对象
        DataScope dataScope = buildDataScope(user);

        // 返回带有数据权限的 UserDetails
        return new CustomUserDetails(user, dataScope);
    }

    /**
     * 构建数据权限对象
     */
    private DataScope buildDataScope(User user) {
        DataScope dataScope = new DataScope();
        dataScope.setUserId(user.getId());

        // 获取用户的最大数据权限类型
        DataScopeType maxDataScopeType = getMaxDataScopeType(user.getId());

        switch (maxDataScopeType) {
            case ALL:
                // 全部数据权限
                dataScope.setAll(true);
                break;

            case THIS_LEVEL_CHILDREN:
                // 本部门及子部门
                Set<Long> descendantIds = deptService.getDescendantIds(user.getDeptId());
                dataScope.getDeptIds().addAll(descendantIds);
                break;

            case THIS_LEVEL:
                // 仅本部门
                dataScope.getDeptIds().add(user.getDeptId());
                break;

            case SELF:
                // 仅本人数据
                dataScope.setSelf(true);
                break;

            case CUSTOMIZE:
                // 自定义部门权限
                Set<Long> customDeptIds = deptService.getCustomDeptIds(user.getId());
                dataScope.getDeptIds().addAll(customDeptIds);
                break;

            default:
                // 默认不过滤
                dataScope.setAll(true);
                break;
        }

        return dataScope;
    }

    /**
     * 获取用户的最大数据权限类型
     */
    private DataScopeType getMaxDataScopeType(Long userId) {
        // 这里需要根据你的实际业务逻辑查询用户角色的数据权限类型
        // 返回权限最大的那个
        return DataScopeType.ALL;
    }
}
```

### 2.7 自定义 UserDetails

```java
package com.example.config;

import com.albedo.java.plugins.datascope.model.DataScope;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final DataScope dataScope; // 数据权限对象

    public CustomUserDetails(User user, DataScope dataScope) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.dataScope = dataScope;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

### 2.8 Controller 层

```java
package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户列表（带数据权限过滤）
     */
    @GetMapping
    public List<User> listUsers(UserQueryDto queryDto) {
        return userService.queryUsers(queryDto);
    }

    /**
     * 查询所有用户（管理员专用，不过滤数据权限）
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listAllUsers(UserQueryDto queryDto) {
        return userService.queryAllUsers(queryDto);
    }
}
```

### 2.9 配置文件

```yaml
# application.yml
albedo:
  datascope:
    enabled: true                    # 启用数据权限
    default-scope-name: dept_id      # 默认权限字段名
    default-creator-name: created_by # 默认创建者字段名
    sql-log-enabled: true            # 启用 SQL 日志（开发环境）

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.example.entity
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

logging:
  level:
    com.albedo.java.plugins.datascope: DEBUG
    com.example.mapper: DEBUG
```

## 3. 不同场景的使用示例

### 3.1 场景一：部门数据隔离

```java
// 部门经理只能查看本部门及子部门的数据
@Service
public class DepartmentService {

    public List<Order> queryDepartmentOrders() {
        User currentUser = getCurrentUser();
        DataScope dataScope = new DataScope();
        dataScope.setScopeName("dept_id");

        // 获取本部门及子部门ID
        Set<Long> deptIds = deptService.getDescendantIds(currentUser.getDeptId());
        dataScope.getDeptIds().addAll(deptIds);

        return orderMapper.selectList(null, dataScope);
    }
}
```

### 3.2 场景二：个人数据隔离

```java
// 普通员工只能查看自己创建的数据
@Service
public class PersonalService {

    public List<Task> queryMyTasks() {
        User currentUser = getCurrentUser();
        DataScope dataScope = DataScopeUtil.createSelf(currentUser.getId());
        return taskMapper.selectList(null, dataScope);
    }
}
```

### 3.3 场景三：自定义数据权限

```java
// 某些角色可以查看特定部门的数据
@Service
public class CustomScopeService {

    public List<Customer> queryScopedCustomers() {
        User currentUser = getCurrentUser();

        // 查询用户可访问的部门列表
        Set<Long> accessibleDeptIds = roleService.getAccessibleDeptIds(currentUser.getId());

        DataScope dataScope = new DataScope();
        dataScope.setScopeName("dept_id");
        dataScope.setDeptIds(accessibleDeptIds);

        return customerMapper.selectList(null, dataScope);
    }
}
```

## 4. 测试示例

```java
package com.example.test;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DataScopeTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testAllDataScope() {
        // 测试全部数据权限
        DataScope dataScope = DataScopeUtil.createAll();
        List<User> users = userMapper.selectList(null, dataScope);
        System.out.println("全部数据数量: " + users.size());
    }

    @Test
    void testDeptDataScope() {
        // 测试部门数据权限
        DataScope dataScope = DataScopeUtil.createDept(1L, 2L, 3L);
        List<User> users = userMapper.selectList(null, dataScope);
        System.out.println("部门数据数量: " + users.size());
    }

    @Test
    void testSelfDataScope() {
        // 测试本人数据权限
        DataScope dataScope = DataScopeUtil.createSelf(1L);
        List<User> users = userMapper.selectList(null, dataScope);
        System.out.println("本人数据数量: " + users.size());
    }

    @Test
    void testNoDataScope() {
        // 测试无数据权限过滤
        List<User> users = userMapper.selectList(null, null);
        System.out.println("无过滤数据数量: " + users.size());
    }
}
```

## 5. 注意事项

1. **SQL 表别名**：如果你的 SQL 使用了表别名，拦截器会自动识别并使用别名
2. **复杂 SQL**：当前不支持 UNION、复杂子查询等，需要使用简单的 SELECT 语句
3. **性能**：数据权限拦截会对每个 SELECT 语句进行解析，建议只在需要的地方使用
4. **多数据源**：如果使用多数据源，需要为每个数据源单独配置拦截器

## 6. 故障排查

### 6.1 启用调试日志

```yaml
logging:
  level:
    com.albedo.java.plugins.datascope.interceptor.DataScopeInterceptor: DEBUG
```

### 6.2 查看 SQL 变换

启用 SQL 日志后，可以在控制台看到原始 SQL 和修改后的 SQL：

```
DEBUG - 数据权限 SQL 原始: SELECT * FROM sys_user
DEBUG - 数据权限 SQL 修改后: SELECT * FROM sys_user WHERE dept_id IN (1, 2, 3)
```

### 6.3 常见问题

1. **数据权限不生效**：检查是否传递了 DataScope 参数
2. **SQL 解析失败**：简化 SQL 语句，添加表别名
3. **性能问题**：只在需要的地方传递 DataScope，避免全局过滤

## 7. 进阶用法

### 7.1 自定义权限字段

```java
// 按区域权限过滤
DataScope regionScope = new DataScope();
regionScope.setScopeName("region_id");
regionScope.getDeptIds().addAll(Arrays.asList(1L, 2L, 3L));
```

### 7.2 组合权限

```java
// 同时过滤部门和创建者
DataScope combinedScope = new DataScope();
combinedScope.setScopeName("dept_id");
combinedScope.getDeptIds().add(1L);
// 注意：当前实现只支持一种过滤方式，如需组合需要自定义拦截器
```

## 8. 总结

`albedo-datascope-spring-boot-starter` 提供了一个灵活、易用的数据权限解决方案：

1. **零侵入**：通过拦截器自动修改 SQL
2. **灵活配置**：支持多种权限类型
3. **易于集成**：标准的 Spring Boot Starter
4. **独立使用**：可单独引入，不依赖 Albedo 其他模块

通过本文档的示例，你应该能够在自己的项目中快速集成和使用数据权限功能。
