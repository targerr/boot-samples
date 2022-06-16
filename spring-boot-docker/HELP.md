### 整合docker

##### 示例

第一步：创建starter工程spring-boot-docker

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

第二步：IndexController

~~~java
package com.example.springbootdocker.controller;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "hello docker!";
    }
}

~~~

第三步:编写Dockerfile

```yaml
FROM openjdk:8-jre-slim

ADD target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

第四步: 验证

##### 1. 手动打包

```shell
# 1.1 打包
mvn clean package -Dmaven.test.skip=true
# 1.2 构建镜像
docker build -t demo .
# 1.3 运行
docker run -d -p 8080:8080 demo   
# 启动服务
docker run -d -p 8080:8080  --name demo -v ~/temp:/temp example/spring-boot-docker 

# 查看日志
docker logs -f demo

# 进入容器
docker exec -it demo sh
# or
docker attach demo
 
```

挂在目录说明：

> ~/temp:/temp

冒号前面为挂在的目录，你这个自定义，比如：e:/temp。

冒号后面为docker容器目录。

##### 2. Maven插件docker-maven-plugin

2.1 插件配置

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <!-- docker -->
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>0.4.13</version>
            <configuration>
                <!-- made of '[a-z0-9-_.]' -->
                <imageName>${project.artifactId}:${project.version}</imageName>
                <dockerDirectory>${project.basedir}</dockerDirectory>
                <resources>
                    <resource>
                        <targetPath>/</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>
    </plugins>
</build>
```

2.2 执行命令

```shell
mvn clean package docker:build 只执行 build 操作

mvn clean package docker:build -DpushImage 执行 build 完成后 push 镜像

mvn clean package docker:build -DpushImageTag 执行 build 并 push 指定 tag 的镜像
#  运行
docker run -d -p 8080:8080 demo   
 
```

##### 3 mac m1

> dockerfile-maven-plugin使用基于 x86 架构的运行时，并且不会在 Apple M1 (Arm) 上运行。

3.1 插件配置

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

        <plugin>
            <groupId>io.fabric8</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>0.38.1</version>
            <executions>
                <execution>
                    <id>build</id>
                    <phase>pre-integration-test</phase>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

3.2 执行命令

```shell
mvn clean package docker:build 
#  运行
docker run -d -p 8080:8080 demo   
 
```

[参考 mac-m1](https://stackoverflow.com/questions/71300031/docker-image-build-failed-on-mac-m1-chip)