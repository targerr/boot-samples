# 数据权限模块使用示例

这是一个完整的 Spring Boot 示例项目，演示如何使用 `albedo-datascope-spring-boot-starter` 数据权限模块。

## 功能特性

- 🎯 **数据权限控制**：根据用户角色自动过滤数据
- 🔐 **Spring Security 集成**：完整的用户认证和授权
- 📊 **多级别权限**：支持全部、部门、个人三种数据权限级别
- 🚀 **开箱即用**：包含完整的示例代码和数据库脚本

## 快速开始

### 1. 准备数据库

执行 `src/main/resources/schema.sql` 脚本创建数据库和测试数据：

```bash
mysql -u root -p < src/main/resources/schema.sql
```

### 2. 修改数据库配置

根据实际情况修改 `application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/datascope_demo?...
    username: root
    password: your_password
```

### 3. 启动应用

```bash
mvn spring-boot:run
```

或直接运行 `DataScopeDemoApplication` 类的 `main` 方法。

### 4. 访问应用

- 应用地址：http://localhost:8080
- API 文档：查看下方接口说明

## 测试账号

| 用户名 | 密码 | 角色 | 数据权限 | 说明 |
|--------|------|------|----------|------|
| admin | admin123 | 管理员 | 全部数据 | 可以查看所有数据 |
| manager | mgr123 | 部门经理 | 部门数据 | 可以查看本部门数据 |
| sales_mgr | mgr123 | 部门经理 | 部门数据 | 可以查看本部门数据 |
| user1 | user123 | 普通用户 | 个人数据 | 只能查看自己创建的数据 |
| user2 | user123 | 普通用户 | 个人数据 | 只能查看自己创建的数据 |
| user3 | user123 | 普通用户 | 个人数据 | 只能查看自己创建的数据 |
| user4 | user123 | 普通用户 | 个人数据 | 只能查看自己创建的数据 |

## API 接口

### 1. 首页信息

```
GET /api/home
```

获取当前登录用户信息和数据权限信息。

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "username": "user1",
    "realName": "研发工程师A",
    "deptId": 5,
    "roles": ["ROLE_USER"],
    "dataScopeInfo": "本人数据权限"
  }
}
```

### 2. 查询用户列表（带数据权限）

```
GET /api/users
```

根据当前用户的数据权限自动过滤用户列表。

**不同角色的查询结果：**
- **admin**：返回所有用户（7 条）
- **manager**：返回研发部用户（5 条）
- **user1**：只返回自己（1 条）

### 3. 查询所有用户（不过滤）

```
GET /api/admin/users/all
```

仅管理员可用，返回所有用户不过滤数据权限。

### 4. 测试数据权限

```
GET /api/test/datascope
```

测试当前用户的数据权限配置。

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "currentUser": "user1",
    "dataScopeType": "SELF",
    "description": "您只能查看自己创建的数据"
  }
}
```

### 5. 查询订单列表

```
GET /api/orders
```

根据当前用户的数据权限自动过滤订单列表。

### 6. 查询我的订单

```
GET /api/orders/my
```

只查询自己创建的订单。

---

## 完整的接口调用说明

### 认证方式

本项目支持两种认证方式：

1. **Session 模式**：登录后使用 Cookie 保持会话
2. **HTTP Basic Auth**：每次请求携带 Basic 认证头

### 初始化数据

首次启动后，需要初始化用户数据（生成正确的 BCrypt 密码哈希）：

```bash
curl -X POST http://localhost:8080/api/init/users
```

---

### 方式一：Session 模式

#### 1. 登录接口

**请求**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{"username":"admin","password":"admin123"}'
```

**响应**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "username": "admin",
    "realName": "系统管理员",
    "deptId": 1,
    "roles": ["ROLE_ADMIN", "ROLE_USER"],
    "dataScopeInfo": "全部数据权限"
  }
}
```

#### 2. 获取当前用户信息

**请求**
```bash
curl -X GET http://localhost:8080/api/auth/current -b cookies.txt
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "username": "admin",
    "realName": "系统管理员",
    "deptId": 1,
    "roles": ["ROLE_ADMIN", "ROLE_USER"],
    "dataScopeInfo": "全部数据权限"
  }
}
```

#### 3. 查询用户列表（带数据权限）

**请求**
```bash
curl -X GET http://localhost:8080/api/users -b cookies.txt
```

---

### 方式二：HTTP Basic Auth

**Base64 编码表**

| 用户名:密码 | Base64 编码 |
|------------|------------|
| admin:admin123 | YWRtaW46YWRtaW4xMjM= |
| manager:mgr123 | bWFuYWdlcjptZ3IxMjM= |
| user1:user123 | dXNlcjE6dXNlcjEyMw== |

#### 1. 首页信息

```bash
curl http://localhost:8080/api/home \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### 2. 查询用户列表

```bash
# 管理员 - 返回所有用户（7条）
curl http://localhost:8080/api/users \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="

# 部门经理 - 只返回本部门用户（4条）
curl http://localhost:8080/api/users \
  -H "Authorization: Basic bWFuYWdlcjptZ3IxMjM="

# 普通用户 - 只返回自己（1条）
curl http://localhost:8080/api/users \
  -H "Authorization: Basic dXNlcjE6dXNlcjEyMw=="
```

#### 3. 获取当前用户信息

```bash
curl http://localhost:8080/api/auth/current \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

---

### 数据权限效果演示

#### 管理员（admin）
- **权限范围**：全部数据
- **SQL 效果**：`SELECT * FROM sys_user WHERE del_flag = 0`
- **返回数量**：7 条记录

#### 部门经理（manager）
- **权限范围**：本部门数据
- **SQL 效果**：`SELECT * FROM sys_user WHERE del_flag = 0 AND dept_id IN (2, 5, 6)`
- **返回数量**：4 条记录（研发部）

#### 普通用户（user1）
- **权限范围**：本人数据
- **SQL 效果**：`SELECT * FROM sys_user WHERE del_flag = 0 AND created_by = 4`
- **返回数量**：1 条记录（自己）

---

### 接口列表汇总

| 接口 | 方法 | 认证 | 说明 |
|------|------|------|------|
| `/api/auth/login` | POST | 否 | 用户登录（JSON 格式） |
| `/api/auth/current` | GET | 是 | 获取当前用户信息 |
| `/api/auth/logout` | POST | 是 | 用户登出 |
| `/api/home` | GET | 是 | 首页信息 |
| `/api/users` | GET | 是 | 查询用户列表（带数据权限） |
| `/api/admin/users/all` | GET | 是 (管理员) | 查询所有用户（不过滤） |
| `/api/test/datascope` | GET | 是 | 测试数据权限 |
| `/api/init/users` | POST | 否 | 初始化用户数据 |
| `/api/orders` | GET | 是 | 查询订单列表（带数据权限） |

---

### 错误处理

#### 401 Unauthorized
```
{"code": 401, "message": "未登录，请先登录", "data": null}
```
**原因**：未提供认证信息或认证失败

#### 登录失败
```
{"code": 500, "message": "用户名或密码错误", "data": null}
```
**原因**：用户名或密码错误，或需要先运行 `/api/init/users`

---

## 数据权限说明

### 权限级别

#### 1. 全部数据权限 (ALL)

- **适用角色**：管理员
- **SQL 效果**：不添加任何 WHERE 条件
- **示例**：`SELECT * FROM sys_user`

#### 2. 部门数据权限 (THIS_LEVEL)

- **适用角色**：部门经理
- **SQL 效果**：添加 `dept_id IN (...)` 条件
- **示例**：`SELECT * FROM sys_user WHERE dept_id IN (2, 5, 6)`

#### 3. 个人数据权限 (SELF)

- **适用角色**：普通用户
- **SQL 效果**：添加 `created_by = ?` 条件
- **示例**：`SELECT * FROM sys_user WHERE created_by = 4`

### 数据权限配置

数据权限在 `CustomUserDetailsService` 中根据用户角色构建：

```java
private DataScope buildDataScope(User user, Set<String> roles) {
    DataScope dataScope = new DataScope();

    if (roles.contains("ROLE_ADMIN")) {
        // 管理员：全部数据权限
        dataScope.setAll(true);
    } else if (roles.contains("ROLE_MANAGER")) {
        // 部门经理：部门数据权限
        dataScope.getDeptIds().add(user.getDeptId());
    } else {
        // 普通用户：个人数据权限
        dataScope.setSelf(true);
    }

    return dataScope;
}
```

## 项目结构

```
datascope-demo/
├── src/main/
│   ├── java/com/example/datascope/
│   │   ├── entity/          # 实体类
│   │   │   ├── User.java
│   │   │   ├── Dept.java
│   │   │   ├── Role.java
│   │   │   └── Order.java
│   │   ├── mapper/          # MyBatis Mapper
│   │   │   ├── UserMapper.java
│   │   │   └── OrderMapper.java
│   │   ├── service/         # 服务层
│   │   │   ├── UserService.java
│   │   │   └── OrderService.java
│   │   ├── controller/      # 控制器
│   │   │   ├── UserController.java
│   │   │   └── OrderController.java
│   │   ├── security/        # 安全配置
│   │   │   ├── CustomUserDetails.java
│   │   │   └── CustomUserDetailsService.java
│   │   ├── config/          # 配置类
│   │   │   └── SecurityConfig.java
│   │   └── DataScopeDemoApplication.java
│   └── resources/
│       ├── application.yml          # 应用配置
│       ├── schema.sql               # 数据库脚本
│       └── mapper/                  # MyBatis XML
│           ├── UserMapper.xml
│           └── OrderMapper.xml
└── pom.xml
```

## 使用方式

### 1. 在 Service 中使用

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> queryUsers() {
        // 从当前用户获取数据权限
        DataScope dataScope = getCurrentUserDataScope();

        // 传递给 Mapper
        return userMapper.selectListWithDataScope(dataScope);
    }
}
```

### 2. 使用工具类

```java
// 创建全部数据权限
DataScope allScope = DataScopeUtil.createAll();

// 创建部门数据权限
DataScope deptScope = DataScopeUtil.createDept(1L, 2L, 3L);

// 创建个人数据权限
DataScope selfScope = DataScopeUtil.createSelf(userId);
```

### 3. Mapper XML 配置

```xml
<select id="selectListWithDataScope" resultMap="BaseResultMap">
    SELECT *
    FROM sys_user
    WHERE del_flag = 0
    <!-- DataScope 拦截器会自动添加数据权限条件 -->
</select>
```

## 配置说明

### application.yml

```yaml
# 数据权限配置
albedo:
  datascope:
    enabled: true                    # 启用数据权限
    default-scope-name: dept_id      # 默认权限字段名
    default-creator-name: created_by # 默认创建者字段名
    sql-log-enabled: true            # 启用 SQL 日志
```

## 测试步骤

### 1. 测试管理员权限

```bash
# 登录 admin 用户
# 访问 http://localhost:8080/api/users
# 应该返回所有用户（7 条）
```

### 2. 测试部门经理权限

```bash
# 登录 manager 用户（研发部经理）
# 访问 http://localhost:8080/api/users
# 应该只返回研发部的用户（3 条：admin, manager, user1, user2）
```

### 3. 测试普通用户权限

```bash
# 登录 user1 用户（研发工程师）
# 访问 http://localhost:8080/api/users
# 应该只返回自己（1 条：user1）
```

### 4. 测试订单数据权限

```bash
# 不同用户访问 /api/orders
# 应该根据数据权限返回不同的订单列表
```

## 常见问题

### Q1: 数据权限不生效？

检查项：
1. `albedo.datascope.enabled` 是否为 `true`
2. Mapper 方法是否传递了 DataScope 参数
3. DataScope 对象的属性是否正确设置

### Q2: 如何查看 SQL 日志？

启用 SQL 日志：
```yaml
albedo:
  datascope:
    sql-log-enabled: true
```

### Q3: 如何自定义数据权限？

实现 `CustomUserDetailsService` 的 `buildDataScope` 方法：
```java
private DataScope buildDataScope(User user, Set<String> roles) {
    // 自定义数据权限逻辑
    DataScope dataScope = new DataScope();
    // ... 根据业务规则设置
    return dataScope;
}
```

## 技术栈

- Spring Boot 2.7.18
- Spring Security
- MyBatis Plus 3.5.3.1
- MySQL 8.0
- Druid 数据源
- Lombok
- Hutool

## 相关文档

- [数据权限模块 README](../../albedo-plugins/albedo-datascope-spring-boot-starter/README.md)
- [数据权限模块使用指南](../../albedo-plugins/albedo-datascope-spring-boot-starter/USAGE.md)
- [数据权限模块开发文档](../../albedo-plugins/albedo-datascope-spring-boot-starter/DEVELOPMENT.md)

## 许可证

Apache License 2.0
