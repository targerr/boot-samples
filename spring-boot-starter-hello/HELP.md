## 1. Spring Boot starter

我们知道Spring Boot大大简化了项目初始搭建以及开发过程，而这些都是通过Spring Boot提供的starter来完成的。品达通用权限系统就是基于Spring Boot进行开发，而且一些基础模块其本质就是starter，所以我们需要对Spring Boot的starter有一个全面深入的了解，这是我们开发品达通用权限系统的必备知识。

### 1.1 starter介绍

spring boot 在配置上相比spring要简单许多, 其核心在于spring-boot-starter, 在使用spring boot来搭建一个项目时, 只需要引入官方提供的starter, 就可以直接使用, 免去了各种配置。starter简单来讲就是引入了一些相关依赖和一些初始化的配置。

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

其中Spring Boot实现自动配置使用的是@Import注解这种方式，AutoConfigurationImportSelector类的selectImports方法返回一组从META-INF/spring.factories文件中读取的bean的全类名，这样Spring Boot就可以加载到这些Bean并完成实例的创建工作。

#### 1.1.3 自动配置总结

我们可以将自动配置的关键几步以及相应的注解总结如下：

1、@Configuration与@Bean：基于Java代码的bean配置

2、@Conditional：设置自动配置条件依赖

3、@EnableConfigurationProperties与@ConfigurationProperties：读取配置文件转换为bean

4、@EnableAutoConfiguration与@Import：实现bean发现与加载