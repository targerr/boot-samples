### Spring Boot Admin

#### 创建SpringBoot Admin Server

1. 创建工程 commerce-admin 并配置pom文件

```xml

<dependencies>
    <!--健康检查-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!-- spring cloud alibaba nacos discovery 依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>2.2.3.RELEASE</version>
    </dependency>
    <!-- SpringBoot Admin -->
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-server</artifactId>
        <version>2.2.0</version>
    </dependency>

    <dependency>
        <groupId>com.example</groupId>
        <artifactId>commerce-mvc-config</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

2. 启动类添加

```java
/**
 * 监控中心服务器启动入口
 */
@EnableAdminServer
@SpringBootApplication
public class CommerceAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceAdminApplication.class, args);
    }
}
```

3. 配置文件

```yaml

server:
  port: 7001
  servlet:
    context-path: /commerce-admin

spring:
  application:
    name: commerce-admin
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        enabled: true # 如果不想使用 Nacos 进行服务注册和发现, 设置为 false 即可
        #server-addr: 172.16.200.249:8848
        server-addr: 127.0.0.1:8848
        # server-addr: 127.0.0.1:8848,127.0.0.1:8849,127.0.0.1:8850 # Nacos 服务器地址
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7
        metadata:
          management:
            context-path: ${server.servlet.context-path}/actuator

  thymeleaf:
    check-template: false
    check-template-location: false
# 暴露端点
management:
  endpoints:
    web:
      exposure:
        include: '*'  # 需要开放的端点。默认值只打开 health 和 info 两个端点。通过设置 *, 可以开放所有端点
  endpoint:
    health:
      show-details: always
# 日志
logging:
  file: /application/applogs/admin.log

```

#### 创建Spring Boot Admin Client

1. 创建工程 commerce-alibaba-nacos-client 并配置pom文件

```xml

<dependencies>
    <!--健康检查-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

2. 配置文件(打开健康检查全部端点)

```yaml
# 暴露端点
management:
  endpoints:
    web:
      exposure:
        include: '*'  # 需要开放的端点。默认值只打开 health 和 info 两个端点。通过设置 *, 可以开放所有端点
  endpoint:
    health:
      show-details: always
# 日志
logging:
  file: /application/applogs/nacos.log

```

**注意:** 模块多,建议配置servlet的context-path属性增加一节路径加以区分不同服务. 如

```yaml
server:
  port: 8080
  servlet:
    context-path: /ecommerce-nacos-client
spring:
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        metadata:
          management:
            # actuator默认访问的还是http://ip:port/actuator
            context-path: ${server.servlet.context-path}/actuator

```

#### 集成spring security

官方说明:
> 默认情况下spring-boot-admin-server-ui提供登录页面和注销按钮。

1. 添加依赖(server端)

```xml
        <!-- 开启登录认证功能 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

```
2. 配置文件填写账号密码
```yaml
spring:
  application:
    name: commerce-admin
  security:
    user:
      name: admin
      password: admin
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        metadata:
          user.name: admin
          user.password: admin


```
3. Security类(以便让其他服务注册)
```java
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    /** 应用上下文路径 */
    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                // 1. 配置所有的静态资源和登录页可以公开访问
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login","/css/**","/js/**","/image/*").permitAll()
                // 2. 其他请求, 必须要经过认证
                .anyRequest().authenticated()
                .and()
                // 3. 配置登录和登出路径
                .formLogin().loginPage(adminContextPath + "/login")
                .successHandler(successHandler)
                .and()
                .logout().logoutUrl(adminContextPath + "/logout")
                .and()
                // 4. 开启 http basic 支持, 其他的服务模块注册时需要使用
                .httpBasic()
                .and()
                // 5. 开启基于 cookie 的 csrf 保护
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // 6. 忽略这些路径的 csrf 保护以便其他的模块可以实现注册
                .ignoringAntMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );
    }

}

```

4. 其他client端 只需修改配置文件
```yaml
spring:
  application:
    name: commerce-nacos-client
  cloud:
    nacos:
      # 服务注册发现
      discovery:
        metadata:
          management:
            context-path: ${server.servlet.context-path}/actuator
          user.name: admin
          user.password: admin

```

[Spring Boot Admin 参考文档](https://juejin.cn/post/6956483441227464740)
