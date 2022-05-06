## 1. Spring Boot starter

我们知道Spring Boot大大简化了项目初始搭建以及开发过程，而这些都是通过Spring Boot提供的starter来完成的。品达通用权限系统就是基于Spring
Boot进行开发，而且一些基础模块其本质就是starter，所以我们需要对Spring Boot的starter有一个全面深入的了解，这是我们开发品达通用权限系统的必备知识。

### 1.1 starter介绍

spring boot 在配置上相比spring要简单许多, 其核心在于spring-boot-starter, 在使用spring boot来搭建一个项目时, 只需要引入官方提供的starter, 就可以直接使用,
免去了各种配置。starter简单来讲就是引入了一些相关依赖和一些初始化的配置。

Spring官方提供了很多starter，第三方也可以定义starter。为了加以区分，starter从名称上进行了如下规范：

- [ ] Spring官方提供的starter名称为：spring-boot-starter-xxx

  例如Spring官方提供的spring-boot-starter-web

- [ ] 第三方提供的starter名称为：xxx-spring-boot-starter

  例如由mybatis提供的mybatis-spring-boot-starter

### 1.2 starter原理

Spring Boot之所以能够帮我们简化项目的搭建和开发过程，主要是基于它提供的起步依赖和自动配置。

#### 1.1.1 起步依赖

起步依赖，其实就是将具备某种功能的坐标打包到一起，可以简化依赖导入的过程。例如，我们导入spring-boot-starter-web这个starter，则和web开发相关的jar包都一起导入到项目中了。如下图所示：

#### 1.1.2 自动配置

自动配置，就是无须手动配置xml，自动配置并管理bean，可以简化开发过程。那么Spring Boot是如何完成自动配置的呢？

自动配置涉及到如下几个关键步骤：

- [ ] 基于Java代码的Bean配置
- [ ] 自动配置条件依赖
- [ ] Bean参数获取
- [ ] Bean的发现
- [ ] Bean的加载

我们可以通过一个实际的例子mybatis-spring-boot-starter来说明自动配置的实现过程。

##### 1.1.1.1 基于Java代码的Bean配置

这些注解是spring boot特有的，常见的条件依赖注解有：

| 注解                            | 功能说明                                             |
| ------------------------------- | ---------------------------------------------------- |
| @ConditionalOnBean              | 仅在当前上下文中存在某个bean时，才会实例化这个Bean   |
| @ConditionalOnClass             | 某个class位于类路径上，才会实例化这个Bean            |
| @ConditionalOnExpression        | 当表达式为true的时候，才会实例化这个Bean             |
| @ConditionalOnMissingBean       | 仅在当前上下文中不存在某个bean时，才会实例化这个Bean |
| @ConditionalOnMissingClass      | 某个class在类路径上不存在的时候，才会实例化这个Bean  |
| @ConditionalOnNotWebApplication | 不是web应用时才会实例化这个Bean                      |
| @AutoConfigureAfter             | 在某个bean完成自动配置后实例化这个bean               |
| @AutoConfigureBefore            | 在某个bean完成自动配置前实例化这个bean               |

##### 1.1.1.2 Bean的加载

在Spring Boot应用中要让一个普通类交给Spring容器管理，通常有以下方法：

1、使用 @Configuration与@Bean 注解

2、使用@Controller @Service @Repository @Component 注解标注该类并且启用@ComponentScan自动扫描

3、使用@Import 方法

其中Spring
Boot实现自动配置使用的是@Import注解这种方式，AutoConfigurationImportSelector类的selectImports方法返回一组从META-INF/spring.factories文件中读取的bean的全类名，这样Spring
Boot就可以加载到这些Bean并完成实例的创建工作。

#### 1.1.3 自动配置总结

我们可以将自动配置的关键几步以及相应的注解总结如下：

1、@Configuration与@Bean：基于Java代码的bean配置

2、@Conditional：设置自动配置条件依赖

3、@EnableConfigurationProperties与@ConfigurationProperties：读取配置文件转换为bean

4、@EnableAutoConfiguration与@Import：实现bean发现与加载

### 2.3 自定义starter

本小节我们通过自定义两个starter来加强starter的理解和应用。

#### 2.3.1 案例一

##### 2.3.1.1 开发starter

第一步：创建starter工程spring-boot-starter-hello并配置pom.xml文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-starter-hello</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-starter-hello</name>
    <description>spring-boot-starter-hello</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
    </dependencies>

</project>

~~~

第二步：创建配置属性类HelloProperties

~~~java
package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 *读取配置文件转换为bean
 * */
@ConfigurationProperties(prefix = "hello")
public class HelloProperties {
    private String name;
    private String address;

    // set..
    // get..
}
~~~

第三步：创建服务类HelloService

~~~java
package com.example.service;

public class HelloService {
    private String name;
    private String address;

    public HelloService(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String sayHello() {
        return "你好！我的名字叫 " + name + "，我来自 " + address;
    }
}
~~~

第四步：创建自动配置类HelloServiceAutoConfiguration

~~~java
package com.example.config;

import com.example.service.HelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * 配置类，基于Java代码的bean配置
 * */

@Configuration
@EnableConfigurationProperties(HelloProperties.class)
public class HelloServiceAutoConfiguration {
    private HelloProperties helloProperties;

    //通过构造方法注入配置属性对象HelloProperties
    public HelloServiceAutoConfiguration(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    //实例化HelloService并载入Spring IoC容器
    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService() {
        return new HelloService(helloProperties.getName(), helloProperties.getAddress());
    }
}
~~~

第五步：在resources目录下创建META-INF/spring.factories

~~~properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.config.HelloServiceAutoConfiguration
~~~

至此starter已经开发完成了，可以将当前starter安装到本地maven仓库供其他应用来使用。

##### 2.3.1.2 使用starter

第一步：创建maven工程spring-boot-starter-myhello并配置pom.xml文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-starter-myhello</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--导入自定义starter-->
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>spring-boot-starter-hello</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
~~~

第二步：创建application.yml文件

~~~yaml
server:
  port: 8080
hello:
  name: xiaoming
  address: beijing
~~~

第三步：创建HelloController

~~~java
package com.example.controller;

import com.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    //HelloService在我们自定义的starter中已经完成了自动配置，所以此处可以直接注入
    @Autowired
    private HelloService helloService;

    @GetMapping("/say")
    public String sayHello() {
        return helloService.sayHello();
    }
}
~~~

第四步：创建启动类HelloApplication

~~~java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
~~~

执行启动类main方法，访问地址http://localhost:8080/hello/say

#### 2.3.2 案例二

在前面的案例一中我们通过定义starter，自动配置了一个HelloService实例。本案例我们需要通过自动配置来创建一个拦截器对象，通过此拦截器对象来实现记录日志功能。

我们可以在案例一的基础上继续开发案例二。

##### 2.3.2.1 开发starter

第一步：在hello-spring-boot-starter的pom.xml文件中追加如下maven坐标

~~~xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
    </dependency>
</dependencies>
~~~

第二步：自定义MyLog注解

~~~java
package com.example.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
    /**
     * 方法描述
     */
    String desc() default "";
}
~~~

第三步：自定义日志拦截器MyLogInterceptor

~~~java
package com.example.log;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 日志拦截器
 */
public class MyLogInterceptor extends HandlerInterceptorAdapter {
    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();//获得被拦截的方法对象
        MyLog myLog = method.getAnnotation(MyLog.class);//获得方法上的注解
        if (myLog != null) {
            //方法上加了MyLog注解，需要进行日志记录
            long startTime = System.currentTimeMillis();
            startTimeThreadLocal.set(startTime);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();//获得被拦截的方法对象
        MyLog myLog = method.getAnnotation(MyLog.class);//获得方法上的注解
        if (myLog != null) {
            //方法上加了MyLog注解，需要进行日志记录
            long endTime = System.currentTimeMillis();
            Long startTime = startTimeThreadLocal.get();
            long optTime = endTime - startTime;

            String requestUri = request.getRequestURI();
            String methodName = method.getDeclaringClass().getName() + "." +
                    method.getName();
            String methodDesc = myLog.desc();

            System.out.println("请求uri：" + requestUri);
            System.out.println("请求方法名：" + methodName);
            System.out.println("方法描述：" + methodDesc);
            System.out.println("方法执行时间：" + optTime + "ms");
        }
    }
}
~~~

第四步：创建自动配置类MyLogAutoConfiguration，用于自动配置拦截器、参数解析器等web组件

~~~java
package com.example.config;

import com.example.log.MyLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，用于自动配置拦截器、参数解析器等web组件
 */

@Configuration
public class MyLogAutoConfiguration implements WebMvcConfigurer {
    //注册自定义日志拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyLogInterceptor());
    }
}
~~~

第五步：在spring.factories中追加MyLogAutoConfiguration配置

~~~properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.config.HelloServiceAutoConfiguration,\
com.example.config.MyLogAutoConfiguration
~~~

注意：我们在hello-spring-boot-starter中追加了新的内容，需要重新打包安装到maven仓库。

##### 2.3.2.2 使用starter

在myapp工程的Controller方法上加入@MyLog注解

~~~java
package com.example.controller;

import com.example.log.MyLog;
import com.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    //HelloService在我们自定义的starter中已经完成了自动配置，所以此处可以直接注入
    @Autowired
    private HelloService helloService;

    @MyLog(desc = "sayHello方法") //日志记录注解
    @GetMapping("/say")
    public String sayHello() {
        return helloService.sayHello();
    }
}
~~~

访问地址：http://localhost:8080/hello/say，查看控制台输出：

~~~makefile
请求uri：/hello/say
请求方法名：com.example.controller.HelloController.sayHello
方法描述：sayHello方法
方法执行时间：36ms
~~~