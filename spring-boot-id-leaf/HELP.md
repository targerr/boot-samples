### 获取分布式Id

##### 示例

第一步：创建starter工程spring-boot-id-leaf并配置pom.xml文件
- 下载leaf项目 mvn clean install -DskipTests
~~~xml
	<dependencies>
    <dependency>
        <artifactId>leaf-boot-starter</artifactId>
        <groupId>com.sankuai.inf.leaf</groupId>
        <version>1.0.1-RELEASE</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.10</version>
    </dependency>
    <!--zk-->
    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-recipes</artifactId>
        <version>4.0.1</version>
        <exclusions>
            <exclusion>
                <artifactId>log4j</artifactId>
                <groupId>log4j</groupId>
            </exclusion>
        </exclusions>
    </dependency>

</dependencies>

~~~

第二步：创建leaf.properties文件

~~~yaml
leaf.name=com.sankuai.leaf.opensource.test
leaf.segment.enable=true
leaf.segment.url=jdbc:mysql://127.0.0.1:3306/leaf?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
leaf.segment.username=root
leaf.segment.password=root

leaf.snowflake.enable=true
leaf.snowflake.address=zk.springboot.cn
leaf.snowflake.port=2181
~~~


第三步：IdContoller

~~~java

package com.example.controller;

import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.service.SegmentService;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdContoller {

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private SnowflakeService snowflakeService;

    @GetMapping("/segment")
    public Long segment() {
        return segmentService.getId("leaf-segment-test").getId();
    }

    @GetMapping("/snowflake")
    public Result snowflake() {
        return snowflakeService.getId("example");
    }
}

~~~

第五步：创建启动类SpringBootIdLeafApplication

~~~java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootIdLeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIdLeafApplication.class, args);
    }

}

~~~

执行启动类main方法， 号段:访问地址127.0.0.1:8080/segment
执行启动类main方法， 雪花:访问地址127.0.0.1:8080/segment

### [文档链接](https://github.com/Meituan-Dianping/Leaf/blob/master/README_CN.md)