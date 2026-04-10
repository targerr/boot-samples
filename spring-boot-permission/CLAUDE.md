# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Enterprise-level RBAC (Role-Based Access Control) permission management system with code generator. Full-stack implementation with backend REST API and Vue3 SPA frontend.

**Tech Stack:**
- Backend: Spring Boot 3.2.1, JDK 17, MyBatis-Plus 3.5.6, Sa-Token 1.45.0, Knife4j 4.4.0
- Frontend: Vue3, Element Plus, Vite 5, Axios
- Database: MySQL 8.0

## Development Commands

### Backend
```bash
# Compile
mvn compile

# Run (default profile: dev, port: 8080)
mvn spring-boot:run

# Package
mvn clean package

# Access API documentation
# http://localhost:8080/doc.html
```

### Frontend
```bash
cd permission-ui

# Install dependencies
npm install

# Dev server (port: 5173, proxies /api → http://localhost:8080)
npm run dev

# Build for production
npm run build
```

### Database
```bash
# Create database and import initial data
mysql -u root -p < init.sql
```

**Default login credentials:** `Admin` / `123456` (NOT 12345678 - the hash in init.sql is MD5 of "123456")

## Architecture

### Backend Package Structure

```
com.mamba
├── Application.java           # Spring Boot entry point
├── common/                     # Shared utilities and base classes
│   ├── result/                # R (unified response), PageResult
│   ├── exception/             # BusinessException, GlobalExceptionHandler
│   ├── base/                  # BaseEntity (operator/operateTime/operateIp), BaseController
│   ├── handler/               # MyBatisMetaHandler (auto-fill audit fields)
│   └── util/                  # IpUtil, TreeUtil (generic tree builder)
├── config/                     # Configuration classes
│   ├── MyBatisPlusConfig      # Pagination, auto-fill registration
│   ├── SaTokenConfig          # Route interceptor for /api/** (excludes /api/auth/login)
│   ├── StpInterfaceImpl       # Sa-Token permission/role provider
│   ├── Knife4jConfig          # Swagger/OpenAPI documentation
│   └── WebMvcConfig           # CORS configuration
├── generator/                  # Code generator module
│   ├── controller/            # GeneratorController
│   ├── service/               # CodeGeneratorService (uses MyBatis-Plus Generator)
│   ├── vo/                    # TableInfoVO, GeneratorConfigVO
│   └── config/                # GeneratorProperties
├── system/                     # Business modules (8 entities)
│   ├── controller/            # REST controllers with Sa-Token annotations
│   ├── service/               # Service interfaces + implementations
│   ├── mapper/                # MyBatis-Plus mappers (extends BaseMapper)
│   ├── entity/                # JPA entities (7 extend BaseEntity, SysLog standalone)
│   ├── vo/                    # View objects for API responses
│   └── dto/                   # Request DTOs with validation
└── resources/
    ├── application.yml        # Main config
    ├── application-dev.yml    # Dev environment (MySQL, Druid)
    └── templates/generator/   # Velocity templates for code generation
```

### Frontend Structure

```
permission-ui/src/
├── main.js                    # Vue app bootstrap (Element Plus, icons, router, directive)
├── App.vue                    # Root component
├── permission.js              # v-permission directive setup
├── router/index.js            # Routes + navigation guard (token check)
├── api/                       # API wrappers around axios
│   ├── auth.js               # login, logout, getUserInfo
│   ├── system.js             # All system management CRUD APIs
│   └── generator.js          # Code generator APIs
├── utils/                     # Utilities
│   ├── auth.js               # Token storage (localStorage key: permission_token)
│   └── request.js            # Axios instance + interceptors (token injection, error handling)
├── layout/                    # Layout components
│   ├── MainLayout.vue        # Sidebar + header + main content area
│   └── Sidebar.vue           # Navigation menu with router
└── views/                     # Page components
    ├── login/                # Login page
    ├── dashboard/            # Dashboard with stats
    ├── system/               # User, Dept, ACL, Role, Log management
    ├── generator/            # Code generator interface
    └── error/                # 403, 404 pages
```

### Database Schema (8 Tables)

**Tree-structured (parent_id + level dotted path):**
- `sys_dept` - Department hierarchy
- `sys_acl_module` - Permission module hierarchy

**Core entities:**
- `sys_user` - Users (status: 1=normal, 0=frozen, 2=deleted)
- `sys_acl` - Permission points (type: 1=menu, 2=button, 3=other)
- `sys_role` - Roles (type: 1=admin, 2=other)

**Relationship tables:**
- `sys_role_acl` - Role ↔ Permission (many-to-many)
- `sys_role_user` - Role ↔ User (many-to-many)

**Audit:**
- `sys_log` - Change tracking with old_value/new_value (JSON), restore capability (type: 1-7 for different entity types)

## Key Design Patterns

### Sa-Token Integration
- **Login flow:** `SysUserService.login()` validates credentials → `StpUtil.login(userId)` → token returned to frontend
- **Permission check:** `StpInterfaceImpl` provides `getPermissionList()` (returns ACL URLs) and `getRoleList()`
- **Route protection:** `SaTokenConfig` adds `SaInterceptor` for `/api/**` (excludes `/api/auth/login`)
- **Controller annotations:** `@SaCheckLogin` for authenticated endpoints

### Auto-Fill Mechanism
All entities extending `BaseEntity` have these fields auto-filled by `MyBatisMetaHandler`:
- `operator` - from `StpUtil.getLoginIdAsString()` or "system"
- `operateTime` - `LocalDateTime.now()`
- `operateIp` - from `IpUtil.getIpAddr(request)` or "127.0.0.1"

### Tree Structure Pattern
- `level` column stores dotted path: root="0", child="0.1", grandchild="0.1.1"
- `TreeUtil.buildTree()` generic method groups children by parentId and recursively assembles tree
- Used for: department tree, permission module tree

### Code Generator
- Uses MyBatis-Plus `FastAutoGenerator` with Velocity templates
- Templates located in `src/main/resources/templates/generator/`
- Generates: Entity, Mapper, Service, Controller, VO, DTO, Vue pages, API files
- Access via: `GET /api/generator/tables` → `POST /api/generator/generate`

### API Response Format
All endpoints return `R<T>` wrapper:
```json
{ "code": 200, "msg": "success", "data": {...} }
```
- Pagination endpoints return `PageResult<T>` with `total`, `data`, `pageNum`, `pageSize`
- Error responses return appropriate codes (400 for validation, 401 for unauthorized, 500 for server errors)

### Frontend State Management
- Reactive store in `src/store/index.js` using Vue 3 `reactive()`
- Token stored in `localStorage.getItem('permission_token')`
- Permissions stored as ACL ID array in `localStorage.getItem('permissions')`
- Note: Store exists but is not fully integrated - MainLayout directly calls API

### Frontend Permission Directive
`v-permission` directive (defined in `src/permission.js`):
- Checks if permission ID exists in localStorage permissions array
- Removes element from DOM if user lacks permission
- Usage: `<el-button v-permission="7">Delete</el-button>` (where 7 is an ACL ID)

## Important Notes

### Password Hashing
- Passwords are MD5 hashed using Hutool: `DigestUtil.md5Hex(password)`
- Default Admin password in database is MD5("123456"), NOT MD5("12345678")

### Switch Statement Pattern (Java 17)
Service implementations use switch expressions for type/description mapping:
```java
return switch (type) {
    case 1 -> "部门";
    case 2 -> "用户";
    // ...
};
```

### DTO Validation
All DTOs use Jakarta validation annotations (`@NotBlank`, `@NotNull`, `@NotEmpty`)
Controllers use `@Valid` on `@RequestBody` parameters

### MyBatis-Plus Configuration
- Mapper locations: `classpath*:/mapper/**/*.xml`
- Camel case conversion enabled
- SQL logging: `StdOutImpl` (dev mode)
- ID strategy: `AUTO` (database auto-increment)

### Code Style Requirements
- **MANDATORY:** All business code uses Lambda + Stream API style (no traditional for-loops for collections)
- `Optional` for null safety
- `@Resource` (Jakarta) for dependency injection (not `@Autowired`)
- Service layer extends `ServiceImpl<Mapper, Entity>` and implements custom service interface
- Controllers return `R.ok(data)` or `R.fail(msg)` for consistency
