## Nacos构建配置中心

### 第一步：创建starter工程nacos-config并配置pom.xml文件

```xml

<dependencies>
    <!--Nacos配置中心的依赖-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
</dependencies>

```

### 第二步： 配置文件

#### 1. 普通配置

- 访问nacos,在配置列表新增data-id=push-dev.yaml,将原有应用中的application.yml内容加入到"配置内容中"

1.1 nacos push-dev.yaml

```yaml
push:
  # 个推配置
  getui:
    appId: 34w1ABjqE66S6bo8AYcqe6
    appKey: IXVCKs6fT29bZ93Up4DzN5
    masterSecret: 0101
```

1.2 bootstrap.yaml

```yaml
spring:
  application:
    name: push
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 172.16.200.249:8848
      config:
        server-addr: 172.16.200.249:8848
        file-extension: yaml # dataid 的名称就是application的name加file-extension
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7 # 环境的id
        group: DEFAULT_GROUP
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/7706bdea7a5d4ef3933544f9c5a623ab.png)

##### 配置详情图

![在这里插入图片描述](https://img-blog.csdnimg.cn/45dc612b5e4641248bb13f148363f58d.png)

#### 2. 共享配置

2.1 所有微服务应用共享配置信息

- 如:所有微服务的JWT秘钥
- 通过设置shared-configs说明全局共享data-id

2.2 bootstrap.ymal

```yaml
cloud:
  nacos:
    config:
      shared-configs: #跨应用全局共享的配置信息
        - data-id: common-${spring.profiles.active}.yml #附加profiles让全局配置也可以区分环境配置
          group: DEFAULT_GROUP
          refresh: true #代表是否允许自动刷新
```

2.3 nacos配置 data-id:common-dev.yml

```yaml
#开发环境下全局共享的配置
app:
  secretKey: 1234567890-1234567890-999999999
```

**注意** 与微服务绑定的配置信息

- 如:应用的名称,字典等基础配置
- 配置优先级: push-{profiles}.yml >  push.yml > 全局共享.yml
- 这是默认规则,只需要创建push.yml无需修改程序便能生效
- 优先级作用范围从小到大逐次降低

3.3 nacos data-id:article-service.yml 保存微服务绑定的配置

```yaml
spring:
  application:
    name: push
```

#### 3. 引入配置

3.1 bootstrap.ymal

```yaml
# 多个配置相关
spring:
  application:
    name: push-dev
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 172.16.200.249:8848
      config:
        server-addr: 172.16.200.249:8848  # 配置中心地址
        file-extension: yaml  # dataid 的名称就是application的name加file-extension
        group: DEFAULT_GROUP  # 测试组
        namespace: 1cbd4445-e494-4011-bccb-7ac4c094f9d7 # 环境的id
        extension-configs:
          # 参数配置
          - data-id: push-dev.yaml
            group: DEFAULT_GROUP
            refresh: true

```

### 第三步： 配置类

```java

@Data
@ConfigurationProperties(prefix = "push.getui")
@Component
public class GetuiProperties {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 应用key
     */
    private String appKey;
    /**
     * 应用秘钥
     */
    private String masterSecret;
}

```

### 第四步： 自动刷新

> @RefreshScope注解实现Nacos配置变更后的自动刷新

- 将应用中的各个配置项剥离到一个独立的类中

```java

@Configuration //说明这是一个Spring JavaConfig配置类
@RefreshScope //通知Spring,这个类中的属性对nacos配置进行监听
// 一旦nacos重新发布新的属性值则自动创建一个新的AppConfig对象替换旧对象实现自动更新
public class AppConfig {
    @Value("${app.secretKey}")
    private String appKey = null;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
```

- 在类上增加@RefreshScope就可以了.原理是这个类中的属性对nacos配置进行监听,一旦nacos重新发布新的属性值则自动创建一个新的AppConfig对象替换旧对象实现自动更新

### 第五步： 测试

```java

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private GetuiProperties getuiProperties;

    @GetMapping("/config")
    public GetuiProperties getuiProperties() {
        return getuiProperties;
    }
}


```

##### TIPS1: 请务必保持nacos data-id 与 {应用名称}-{环境配置}.{扩展名}保持一致

##### TIPS2: 通过修改spring.profiles.active ,可以让应用程序随时在开发/生产的配置中进行切换