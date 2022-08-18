## 整合跨域

### 第一步：创建starter工程spring-boot-cors并配置pom.xml文件

```xml

<dependencies>

</dependencies>

```

### 第二步： 配置文件

```yaml
logging:
  level:
    com.example: debug
```

### 第三步： 配置类

#### 1. 返回新的 CorsFilter

```java
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
        //是否发送 Cookie
        config.setAllowCredentials(true);
        //放行哪些请求方式
        config.addAllowedMethod("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        // config.addExposedHeader("*");
        config.addExposedHeader("Content-Type");
        config.addExposedHeader("X-Requested-With");
        config.addExposedHeader("accept");
        config.addExposedHeader("Origin");
        config.addExposedHeader("Access-Control-Request-Method");
        config.addExposedHeader("Access-Control-Request-Headers");
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }
}
```

#### 2.重写 WebMvcConfigurer(全局跨域)

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //项目中的所有接口都支持跨域
        registry.addMapping("/**")
                //所有地址都可以访问，也可以配置具体地址
                .allowedOrigins("*")
                .allowCredentials(true)
                //"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
```
#### 3. 使用注解 (局部跨域)
```java
@RestController
@CrossOrigin(origins = "*")
public class CorsController {
    @GetMapping("/login")
    public String login(String name, String age, HttpSession session) {
        // 业务验证......
        session.setAttribute("user", name + ":" + age);

        return "login success!";
    }

    @GetMapping("/info")
    public String info(HttpSession session) {
        final Object user = session.getAttribute("user");

        return user.toString();
    }
}
```
#### [参考](https://mp.weixin.qq.com/s/gU0Is2Pd7HhvmOukQJdBxQ)




