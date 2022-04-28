### springboot整合 swagger

#### swagger常用注解

| 注解               | 说明                                                     |
| ------------------ | -------------------------------------------------------- |
| @Api               | 用在请求的类上，例如Controller，表示对类的说明           |
| @ApiModel          | 用在类上，通常是实体类，表示一个返回响应数据的信息       |
| @ApiModelProperty  | 用在属性上，描述响应类的属性                             |
| @ApiOperation      | 用在请求的方法上，说明方法的用途、作用                   |
| @ApiImplicitParams | 用在请求的方法上，表示一组参数说明                       |
| @ApiImplicitParam  | 用在@ApiImplicitParams注解中，指定一个请求参数的各个方面 |

#### 案例

第一步：创建maven工程swagger_demo并配置pom.xml文件

~~~xml

<dependencies>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.9.2</version>
    </dependency>

</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
server:
  port: 8080
~~~

第三步： 创建实体类User和Menu

~~~java
package cn.itcast.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户实体")
public class User {
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private int age;
    @ApiModelProperty(value = "地址")
    private String address;
}
~~~

第四步：创建UserController

~~~java

import cn.itcast.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户控制器")
public class UserController {
    @GetMapping("/getUsers")
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户信息")
    public List<User> getAllUsers() {
        User user = new User();
        user.setId(100);
        user.setName("itcast");
        user.setAge(20);
        user.setAddress("bj");
        List<User> list = new ArrayList<>();
        list.add(user);
        return list;
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增用户", notes = "新增用户信息")
    public String save(@RequestBody User user) {
        return "OK";
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户", notes = "修改用户信息")
    public String update(@RequestBody User user) {
        return "OK";
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除用户", notes = "删除用户信息")
    public String delete(int id) {
        return "OK";
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码",
                    required = true, type = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数",
                    required = true, type = "Integer"),
    })
    @ApiOperation(value = "分页查询用户信息")
    @GetMapping(value = "page/{pageNum}/{pageSize}")
    public String findByPage(@PathVariable Integer pageNum,
                             @PathVariable Integer pageSize) {
        return "OK";
    }
}
~~~

第五步：创建配置类SwaggerAutoConfiguration

~~~java

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerAutoConfiguration {
    @Bean
    public Docket createRestApi() {
        log.info("开始加载Swagger2...");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                //扫描指定包中的swagger注解
                //.apis(RequestHandlerSelectors.basePackage("com.example.controller.UserController"))
                //扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot整合Swagger2")
                .description("接口文档")
                .termsOfServiceUrl("http://blog.exrick.cn")
                .contact(new Contact("springboot整合项目", "", ""))
                .version("1.0.0")
                .build();
    }
}
~~~

第六步：创建启动类SwaggerDemoApplication

~~~java
@SpringBootApplication
public class SpringBootUiSwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootUiSwaggerApplication.class, args);
    }

}

~~~

执行启动类main方法启动项目，访问地址：http://localhost:8008/swagger-ui.html