# 数据权限示例项目快速启动指南

## 一、项目概览

这是一个完整的 Spring Boot 示例项目，演示 `albedo-datascope-spring-boot-starter` 数据权限模块的使用。

**项目位置**: `examples/datascope-demo/`

## 二、快速启动（5分钟）

### Step 1: 创建数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source /Users/wanggaoshuai/develop/IdeaProjects/gitee/albedo/examples/datascope-demo/src/main/resources/schema.sql
```

或直接执行：
```bash
mysql -u root -p < /Users/wanggaoshuai/develop/IdeaProjects/gitee/albedo/examples/datascope-demo/src/main/resources/schema.sql
```

### Step 2: 修改数据库配置

编辑 `examples/datascope-demo/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    username: root        # 改成你的 MySQL 用户名
    password: your_password  # 改成你的 MySQL 密码
```

### Step 3: 启动应用

```bash
cd /Users/wanggaoshuai/develop/IdeaProjects/gitee/albedo/examples/datascope-demo
mvn spring-boot:run
```

看到以下输出表示启动成功：

```
====================================================
   数据权限示例应用启动成功！
   访问地址: http://localhost:8080

   测试账号:
   - admin / admin123   (管理员，全部数据权限)
   - manager / mgr123   (部门经理，部门数据权限)
   - user1 / user123    (普通用户，本人数据权限)
====================================================
```

### Step 4: 测试接口

使用浏览器或 API 测试工具（如 Postman）访问以下接口：

#### 4.1 测试首页信息
```
GET http://localhost:8080/api/home
```

#### 4.2 测试用户查询（带数据权限）
```
GET http://localhost:8080/api/users
```

**预期结果：**
- admin 用户：看到所有用户（7 条）
- manager 用户：只看到研发部用户（3-4 条）
- user1 用户：只看到自己（1 条）

## 三、测试账号说明

| 用户名 | 密码 | 角色 | 部门 | 数据权限 |
|--------|------|------|------|----------|
| admin | admin123 | 管理员 | 总公司 | 全部数据 |
| manager | mgr123 | 部门经理 | 研发部 | 部门数据 |
| sales_mgr | mgr123 | 部门经理 | 销售部 | 部门数据 |
| user1 | user123 | 普通用户 | 研发一组 | 个人数据 |
| user2 | user123 | 普通用户 | 研发二组 | 个人数据 |
| user3 | user123 | 普通用户 | 销售一组 | 个人数据 |
| user4 | user123 | 普通用户 | 销售二组 | 个人数据 |

## 四、核心功能演示

### 1. 数据权限拦截效果

**场景**: 不同用户查询用户列表

**管理员 (admin)**:
```sql
-- 拦截器不修改 SQL
SELECT * FROM sys_user WHERE del_flag = 0
-- 结果: 7 条记录
```

**部门经理 (manager)**:
```sql
-- 拦截器添加部门过滤条件
SELECT * FROM sys_user WHERE del_flag = 0 AND dept_id IN (2, 5, 6)
-- 结果: 4 条记录（研发部）
```

**普通用户 (user1)**:
```sql
-- 拦截器添加创建者过滤条件
SELECT * FROM sys_user WHERE del_flag = 0 AND created_by = 4
-- 结果: 1 条记录（自己）
```

### 2. 代码示例

**Service 层**:
```java
public List<User> queryUsers() {
    // 1. 获取当前用户的数据权限
    DataScope dataScope = getCurrentUserDataScope();

    // 2. 传递给 Mapper
    return userMapper.selectListWithDataScope(dataScope);
}
```

**Mapper XML**:
```xml
<select id="selectListWithDataScope" resultMap="BaseResultMap">
    SELECT * FROM sys_user WHERE del_flag = 0
    <!-- DataScope 拦截器会自动添加数据权限条件 -->
</select>
```

## 五、调试技巧

### 1. 启用 SQL 日志

已在 `application.yml` 中配置：
```yaml
albedo:
  datascope:
    sql-log-enabled: true
```

查看日志可以看到 SQL 的修改过程：
```
DEBUG - 数据权限 SQL 原始: SELECT * FROM sys_user WHERE del_flag = 0
DEBUG - 数据权限 SQL 修改后: SELECT * FROM sys_user WHERE del_flag = 0 AND dept_id IN (2, 5, 6)
```

### 2. 使用 Postman 测试

1. 导入以下环境变量：
   - `baseUrl`: http://localhost:8080

2. 创建请求：
   - GET {{baseUrl}}/api/home
   - GET {{baseUrl}}/api/users
   - GET {{baseUrl}}/api/test/datascope

3. 使用 Basic Authentication 或表单登录

## 六、常见问题

### Q1: 启动报错 "Access denied for user"
**解决**: 检查 `application.yml` 中的数据库用户名和密码是否正确。

### Q2: 数据权限不生效
**解决**:
1. 检查 `albedo.datascope.enabled` 是否为 `true`
2. 查看日志确认拦截器是否执行
3. 确认 Mapper 方法是否传递了 DataScope 参数

### Q3: 登录后返回 403
**解决**: 检查用户角色和权限配置，确保用户有所需角色。

## 七、项目结构

```
datascope-demo/
├── pom.xml                          # Maven 配置
├── README.md                        # 详细文档
├── QUICKSTART.md                    # 本文件
└── src/main/
    ├── java/.../datascope/
    │   ├── entity/                  # 实体类
    │   ├── mapper/                  # MyBatis Mapper
    │   ├── service/                 # 服务层
    │   ├── controller/              # 控制器
    │   ├── security/                # Spring Security 配置
    │   ├── config/                  # 配置类
    │   └── DataScopeDemoApplication.java
    └── resources/
        ├── application.yml          # 应用配置
        ├── schema.sql               # 数据库脚本
        └── mapper/                   # MyBatis XML
```

## 八、扩展阅读

- [完整 README](README.md) - 详细的使用文档
- [数据权限模块文档](../../albedo-plugins/albedo-datascope-spring-boot-starter/README.md)
- [数据权限使用指南](../../albedo-plugins/albedo-datascope-spring-boot-starter/USAGE.md)

## 九、技术支持

如遇问题，请查看：
1. 项目日志（控制台输出）
2. 数据库数据是否正确初始化
3. 配置文件是否正确修改

---

**祝您使用愉快！** 🎉
