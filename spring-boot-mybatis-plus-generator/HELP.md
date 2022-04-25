## spring-boot-mybatis

> 演示mybatis-plus代码生成器,具体代码见 demo。

### pom

```xml

<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.5.2</version>
    </dependency>
    <!--模板引擎-->
    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity-engine-core</artifactId>
        <version>2.3</version>
    </dependency>
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>2.3.31</version>
    </dependency>

</dependencies>

```

### 模板

``` 
package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

}
</#if>

```

### 测试

```java

/**
 * 执行 run
 */
public static void main(String[]args){

        String projectPath=System.getProperty("user.dir")+"/spring-boot-mybatis-plus-generator";
        System.err.println(projectPath);
        FastAutoGenerator.create(new DataSourceConfig.
        Builder("jdbc:mysql://127.0.0.1:3306/springboot","root","root123456"))

        // 全局配置
        .globalConfig((scanner,builder)->builder.author(scanner.apply("请输入作者名称")))
        .globalConfig((scanner,builder)->builder.outputDir(projectPath+"/src/main/java"))
        // 包配置
        .packageConfig((scanner,builder)->builder.parent(scanner.apply("请输入包名")))
        // 策略配置
        .strategyConfig((scanner,builder)->builder.addInclude(scanner.apply("请输入表名，多个表名用,隔开")))
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
        .templateEngine(new FreemarkerTemplateEngine())
        .execute();
        }

```