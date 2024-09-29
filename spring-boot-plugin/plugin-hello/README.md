## 如何自定义自己的Starter组件
### 1. 创建 Maven 项目

首先，创建一个新的 Maven 项目，假设项目名称是 `custom-starter`。

`pom.xml` 文件内容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>demo-starter</artifactId>
    <version>1.1.6</version>
    <name>demo-starter</name>
    <description>demo-starter</description>
    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>2.7.6</spring-boot.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <!-- Spring Boot 的自动装配所需要的依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>

        <!-- 配置文件点击可以跳转实体，主要是为了适配 IDEA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
        </plugins>
    </build>

</project>

```

### 2. 编写自动配置类

在 `src/main/java/com/example/starter/` 目录下创建自动配置类。例如：`CustomStarterAutoConfiguration.java`。

```java
package com.example.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomStarterProperties.class) // 绑定配置属性
public class CustomStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CustomService customService(CustomStarterProperties properties) {
        return new CustomService(properties.getPrefix(), properties.getSuffix());
    }
}
```

### 3. 创建服务类

编写你的业务逻辑类。比如：`CustomService.java`。

```java

public class CustomService {
    private final String prefix;
    private final String suffix;

    public CustomService(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String formatMessage(String message) {
        return prefix + message + suffix;
    }
}
```

### 4. 定义配置属性类

在 `src/main/java/com/example/starter/` 目录下创建配置属性类：`CustomStarterProperties.java`。

```java
@Data
@ConfigurationProperties("example.service")
public class CustomStarterProperties {
    private String prefix = "[";
    private String suffix = "]";

}

```

### 5. 注册自动配置

在 `src/main/resources/META-INF/spring.factories` 文件中声明自动配置类。

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.demostarter.config.ExampleAutoConfigure
```

### 6. 测试自定义 Starter

在另一个 Spring Boot 项目中使用你的自定义 Starter。添加依赖：

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>custom-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

在 `application.yml` 中添加自定义 Starter 的配置：

```yaml
custom:
  service:
    enabled: true
    prefix: "<<"
    suffix: ">>"
```

在 Spring Boot 应用中使用 `CustomService`：

```java
@RestController
public class DemoController {

    @Autowired
    private CustomService customService;

    @GetMapping("/test")
    public String test() {
        return customService.formatMessage("Hello World");
    }
}
```

启动 Spring Boot 应用并访问 `/test`，你会看到输出结果类似：

```bash
<<Hello World>>
```

### 总结

- `CustomStarterAutoConfiguration` 自动配置类负责将自定义服务类注册到 Spring 上下文。
- `CustomStarterProperties` 用于通过 `application.yml` 或 `application.properties` 配置自定义服务的属性。
- `spring.factories` 用于在 Spring Boot 启动时自动加载 Starter 的自动配置类。

通过这些步骤，你就实现了一个自定义的 Spring Boot Starter 组件。


### 相关注解
#### 属性映射注解
- @ConfigurationProperties：配置文件属性值和实体类的映射
- @EnableConfigurationProperties：和 @ConfigurationProperties 配合使用，把 @ConfigurationProperties 修饰的类加入 IoC 容器。

#### 配置Bean注解
- @Configuration：标识该类为配置类，并把该类注入 ioc 容器
- @Bean：一般在方法上使用，声明一个 Bean，Bean 名称默认是方法名称，类型为返回值。

####条件注解
- @Conditional：是根据条件类创建特定的 Bean ，条件类需要实现 Condition 接口，并重写 matches 接口来构造判断条件。该注解是 Spring 4 新提供的注解，按照一定的条件进行判断，满足条件给容器注册 Bean。
- @ConditionalOnBean：容器中存在指定 Bean，才会实例化一个 Bean
- @ConditionalOnMissingBean：容器中不存在指定 Bean，才会实例化一个 Bean
- @ConditionalOnClass：系统中有指定类，才会实例化一个 Bean
- @ConditionalOnMissingClass：系统中没有指定类的 Bean 信息，才会实例化一个 Bean
- @ConditionalOnExpression：当 SpEl 表达式为 true 的时候，才会实例化一个 Bean
- @AutoConfigureAfter：在某个 Bean 完成自动配置后实例化这个 Bean
- @AutoConfigureBefore：在某个 Bean 完成自动配置前实例化这个 Bean
- @ConditionalOnJava：系统中版本是否符合要求
- @ConditionalOnSingleCandidate：当指定的 Bean 在容器中只有一个，或者有多个但是指定了首选的 Bean 时触发实例化
- @ConditionalOnResource：类路径下是否存在指定资源文件
- @ConditionalOnWebApplication：是 web 应用
- @ConditionalOnNotWebApplication：不是 web 应用
- havingValue：比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置。
- matchIfMissing：缺少该配置属性时是否可以加载。如果为true，没有该配置属性时也会正常加载；反之则不会生效。
##### 参考
[如何封装stater](https://zhuanlan.zhihu.com/p/673768728)
[如何自定义自己的Starter组件](https://www.jb51.net/program/31743716k.htm)

