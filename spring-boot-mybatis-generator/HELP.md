## spring-boot-mybatis

> 演示mybatis代码生成器,具体代码见 demo。

### pom
```xml
    <dependencies>
        <!-- mybatis generator -->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.7</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <configuration>
                    <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile>
                    <overwrite>true</overwrite>
                    <verbose>true</verbose>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.6</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>


```
### 🏃 配置文件 generatorConfig.xml

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <!-- 连接数据库jar包的路径-->
    <classPathEntry location="src/main/resources/jar/mysql-connector-java-5.1.6-bin.jar"/>

    <context id="DB2Tables"  targetRuntime="MyBatis3">
        <!--        配置签指分隔符的属性-->
        <property name="beginningDelimiter" value="`"/>
        <!--        配置后置分隔符的属性-->
        <property name="endingDelimiter" value="`"/>
        <!--        设置要使用的Java文件的编码-->
        <property name="javaFileEncoding" value="UTF-8"/>
        <!-- 为模型生成序列化方法-->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <!-- 为生成的Java模型创建一个toString方法 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>
        <!--生成mapper.xml时覆盖原文件-->

        <!--数据库连接参数 -->
        <jdbcConnection
                driverClass="com.mysql.jdbc.Driver"
                connectionURL="jdbc:mysql://127.0.0.1:3306/springboot"
                userId="root"
                password="root123456">
        </jdbcConnection>

        <!-- 非必需，类型处理器，在数据库类型和java类型之间的转换控制-->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 实体类的包名和存放路径 -->
        <!-- targetPackage     指定生成的model生成所在的包名
             targetProject     指定在该项目下所在的路径
         -->
        <javaModelGenerator targetPackage="com.example.entity" targetProject="src/main/java">
            <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
            <property name="enableSubPackages" value="true"/>
            <!-- 是否对model添加 构造函数 -->
            <property name="constructorBased" value="true"/>
            <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
            <property name="trimStrings" value="true"/>
            <!-- 建立的Model对象是否 不可改变  即生成的Model对象不会有 setter方法，只有构造方法 -->
            <property name="immutable" value="false"/>

        </javaModelGenerator>

        <!-- 生成映射文件*.xml的位置-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!-- 生成DAO的包名和位置 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.example.dao" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!-- tableName：数据库中的表名或视图名；domainObjectName：生成的实体类的类名-->
        <table tableName="blog" domainObjectName="Blog"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
               selectByExampleQueryId="false"/>


    </context>
</generatorConfiguration>


```

### jar包
> mysql-connector-java-5.1.6-bin.jar


### 测试

![maven插件配置](https://upload-images.jianshu.io/upload_images/4994935-4ed2b57d8f45e7f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
