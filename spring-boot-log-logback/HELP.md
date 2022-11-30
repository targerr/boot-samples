
## spring-boot-log-logback

> 演示logback,具体代码见 demo。
https://dockone.io/article/1640913

第一步：创建maven工程spring-boot-log-logback并配置pom文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/>
    </parent>
    <groupId>cn.itcast</groupId>
    <artifactId>springboot_logback_demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>1.2.3</version>
        </dependency>

    </dependencies>
</project>
~~~

第二步：在resources下编写logback配置文件logback-base.xml和logback-spring.xml

logback-base.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
    <contextName>logback</contextName>
    <!-- 
		name的值是变量的名称，value的值时变量定义的值
		定义变量后，可以使“${}”来使用变量
	-->
    <property name="log.path" value="d:\\logs" />

    <!-- 彩色日志 -->
    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule 
              conversionWord="clr" 
              converterClass="org.springframework.boot.logging.logback.ColorConverter" />
    <conversionRule 
              conversionWord="wex" converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
    <conversionRule conversionWord="wEx" converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />
    <!-- 彩色日志格式 -->
    <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <!--输出到控制台-->
    <appender name="LOG_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
            <!-- 设置字符集 -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--输出到文件-->
    <appender name="LOG_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${log.path}/logback.log</file>
        <!--日志文件输出格式-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 每天日志归档路径以及格式 -->
            <fileNamePattern>${log.path}/info/log-info-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
    </appender>
</included>
~~~

logback-spring.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--引入其他配置文件-->
    <include resource="logback-base.xml" />
    <!--
    <logger>用来设置某一个包或者具体的某一个类的日志打印级别、
    以及指定<appender>。<logger>仅有一个name属性，
    一个可选的level和一个可选的addtivity属性。
    name:用来指定受此logger约束的某一个包或者具体的某一个类。
    level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，
          如果未设置此属性，那么当前logger将会继承上级的级别。
    addtivity:是否向上级logger传递打印信息。默认是true。
     -->

    <!--开发环境-->
    <springProfile name="dev">
        <logger name="com.example" additivity="false" level="debug">
            <appender-ref ref="LOG_CONSOLE"/>
        </logger>
    </springProfile>
    <!--生产环境-->
    <springProfile name="pro">
        <logger name="com.example" additivity="false" level="info">
            <appender-ref ref="LOG_FILE"/>
        </logger>
    </springProfile>

    <!--
    root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性
    level:设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF 默认是DEBUG
    可以包含零个或多个元素，标识这个appender将会添加到这个logger。
    -->
    <root level="info">
        <appender-ref ref="LOG_CONSOLE" />
        <appender-ref ref="LOG_FILE" />
    </root>
</configuration>
~~~

第三步：编写application.yml

~~~yaml
logging:
  #在Spring Boot项目中默认加载类路径下的logback-spring.xml文件
  config: classpath:logback-spring.xml
  level:
    com.example: info
spring:
  profiles:
    active: dev
~~~

第四步：创建TestController

~~~java
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @PostMapping("/get")
    public Dict get(@RequestBody Map<String, Object> map) {
        log.trace("trace...");
        log.debug("debug...");
        log.info("info...");
        log.warn("warn...");
        log.error("error...");

        return Dict.create().set("json", map);
    }

}
~~~

第五步：创建启动类

~~~java
@SpringBootApplication
@Slf4j
public class SpringBootLogLogbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLogLogbackApplication.class, args);
    }

}

~~~

启动项目，访问地址：http://localhost:9000/test/get

可以看到控制台已经开始输出日志信息。