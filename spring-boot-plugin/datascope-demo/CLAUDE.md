# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 2.7.18 demo application for `albedo-datascope-spring-boot-starter`, demonstrating row-level data permissions based on user roles. The project shows how to automatically filter SQL queries based on whether a user is an admin (all data), department manager (department data), or regular user (self-created data).

## Common Commands

### Build and Run
```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# Run by executing the main class directly
# Run src/main/java/com/example/datascope/DataScopeDemoApplication.java
```

### Database Setup
```bash
# Initialize the database (MySQL required)
mysql -u root -p < src/main/resources/schema.sql

# Configure database connection in src/main/resources/application.yml
# Default: localhost:3306/datascope_demo, username: root, password: root123456
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DataScopeDemoTest
```

## Architecture Overview

### Data Scope Flow
The data permission system works through this flow:

1. **Authentication**: `CustomUserDetailsService` loads user and builds `DataScope` object based on roles
2. **Storage**: `CustomUserDetails` holds the `DataScope` in Spring Security's `SecurityContext`
3. **Service Layer**: Services retrieve `DataScope` from `SecurityContext` and pass to mappers
4. **SQL Interception**: MyBatis interceptor intercepts queries and modifies SQL with WHERE clauses

### Permission Levels
- **ROLE_ADMIN** (`isAll=true`): No WHERE clause added, returns all data
- **ROLE_MANAGER** (`deptIds` set): Adds `dept_id IN (...)` to filter by department
- **ROLE_USER** (`isSelf=true`): Adds `created_by = ?` to filter by creator

### Key Components

| Component | Purpose | Location |
|-----------|---------|----------|
| `CustomUserDetailsService` | Builds DataScope based on user roles during authentication | `src/main/java/com/example/datascope/security/` |
| `CustomUserDetails` | Holds DataScope in SecurityContext | `src/main/java/com/example/datascope/security/` |
| `UserService` | Example of retrieving DataScope and passing to mapper | `src/main/java/com/example/datascope/service/` |
| Mapper XML files | Queries that get auto-modified by interceptor | `src/main/resources/mapper/` |

## Key Patterns

### Using Data Scope in Service Layer
```java
// Get DataScope from SecurityContext
DataScope dataScope = getCurrentUserDataScope();

// Pass to mapper - interceptor will modify SQL
List<User> users = userMapper.selectListWithDataScope(dataScope);
```

### Mapper Method Pattern
Mapper methods that accept a `DataScope` parameter will have their SQL automatically modified:
```java
// Mapper interface
List<User> selectListWithDataScope(@Param("dataScope") DataScope dataScope);
```

### Mapper XML Pattern
Write simple queries without explicit permission conditions:
```xml
<select id="selectListWithDataScope" resultMap="BaseResultMap">
    SELECT * FROM sys_user WHERE del_flag = 0
    <!-- DataScope interceptor adds permission conditions here -->
</select>
```

## Configuration

### Data Scope Settings (`application.yml`)
```yaml
albedo:
  datascope:
    enabled: true                    # Enable/disable data permissions
    default-scope-name: dept_id      # Field name for department filtering
    default-creator-name: created_by # Field name for creator filtering
    sql-log-enabled: true            # Log SQL modifications for debugging
```

### Test Accounts
| Username | Password | Role | Data Permission |
|----------|----------|------|-----------------|
| admin | admin123 | ROLE_ADMIN | All data |
| manager | mgr123 | ROLE_MANAGER | Department data |
| user1 | user123 | ROLE_USER | Self-created data |

## Critical Files

- `src/main/java/com/example/datascope/security/CustomUserDetailsService.java` - Contains `buildDataScope()` method that defines how roles map to permissions
- `src/main/java/com/example/datascope/service/UserService.java` - Shows the pattern for using DataScope in services
- `src/main/resources/mapper/UserMapper.xml` - Example of mapper XML that gets auto-modified
- `src/main/resources/application.yml` - Database and DataScope configuration
- `pom.xml` - Dependencies include albedo-datascope-spring-boot-starter, Spring Security, MyBatis Plus
