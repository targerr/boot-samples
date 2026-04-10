# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

This project uses Maven. The Maven wrapper is not available in the standard PATH location. Use the full path:

```bash
# Build and install to local repository
/Users/wanggaoshuai/.m2/wrapper/dists/apache-maven-3.9.12-bin/5nmfsn99br87k5d4ajlekdq10k/apache-maven-3.9.12/bin/mvn clean install
```

Or add an alias to your shell for convenience.

## Project Architecture

This is a **Spring Boot Starter** for data scope (data permissions) control using MyBatis Plus. It intercepts SQL queries and dynamically injects WHERE clauses based on user permissions.

### Core Components

```
src/main/java/com/albedo/java/plugins/datascope/
├── interceptor/DataScopeInterceptor.java    # Core: MyBatis Plus InnerInterceptor that modifies SQL
├── model/DataScope.java                     # Data permission object passed to queries
├── aspect/DataScopeAspect.java              # Optional AOP support for @DataScopeAnno
├── annotation/DataScopeAnno.java            # Annotation for automatic permission handling
├── config/
│   ├── DataScopeAutoConfiguration.java     # Main auto-configuration
│   ├── DataScopeAspectAutoConfiguration.java # AOP auto-configuration
│   └── DataScopeProperties.java             # Configuration properties
├── enums/DataScopeType.java                 # Permission type enums
└── util/DataScopeUtil.java                  # Factory methods for DataScope objects
```

### How It Works

1. **Interceptor Path** (primary): Service layer creates a `DataScope` object and passes it to Mapper methods. The `DataScopeInterceptor` finds it in parameters and uses JSqlParser to rewrite the SQL WHERE clause.

2. **AOP Path** (optional): With `@DataScopeAnno` on methods and `albedo.datascope.aop-enabled=true`, the `DataScopeAspect` automatically retrieves permissions from Spring Security context.

### Auto-Configuration

The starter supports both Spring Boot 2.x and 3.x:
- `META-INF/spring.factories` for Spring Boot 2.x
- `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` for Spring Boot 3.x

Key configurations from `DataScopeProperties`:
- `albedo.datascope.enabled` - Enable/disable data scope (default: true)
- `albedo.datascope.aop-enabled` - Enable AOP support (default: false)
- `albedo.datascope.default-scope-name` - Default permission field (default: dept_id)
- `albedo.datascope.interceptor-order` - Interceptor execution order (default: -10)

## Key Implementation Details

### DataScope Object
Carries permission information through the call stack:
```java
public class DataScope {
    private String scopeName = "dept_id";      // Field name for filtering
    private String creatorName = "created_by"; // Creator field for self-data
    private Set<Long> deptIds;                 // Department IDs for dept-based filtering
    private boolean isAll;                     // Skip filtering when true
    private boolean isSelf;                    // Filter by creator when true
    private Long userId;                       // Current user ID
    private String tableAlias;                 // Table alias for complex queries
}
```

### SQL Modification Logic
The interceptor only processes SELECT statements with a `DataScope` parameter:
- **Dept filtering**: Adds `{tableAlias}{scopeName} IN (...)` to WHERE
- **Self filtering**: Adds `{tableAlias}{creatorName} = {userId}` to WHERE
- **No filtering**: When `isAll=true` or no DataScope parameter

### AOP Integration Requirements
For `@DataScopeAnno` to work:
1. Include `spring-boot-starter-aop` dependency
2. Set `albedo.datascope.aop-enabled=true`
3. Implement `DataScopeAspect.DataScopeProvider` interface in UserDetails
4. Return DataScope from `getDataScope()` method

## Important Constraints

- **SQL Support**: Only `PlainSelect` type SQL is supported. UNION and complex subqueries are not supported and will be logged as warnings.
- **Performance**: The interceptor parses every SELECT statement. Only pass DataScope when needed.
- **Interceptor Order**: Default is `-10` to execute before pagination. Adjust if using other interceptors.
- **Optional Dependencies**: AOP and Security dependencies are optional — the starter works without them in manual mode.

## Version Compatibility

- MyBatis Plus: 3.5.3.1
- Spring Boot: 2.7.8 (compatible with 2.7.x and 3.x)
- JSqlParser: 4.5
- Java: 8+
