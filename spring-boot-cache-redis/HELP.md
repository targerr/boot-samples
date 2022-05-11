### 整合redis

##### 示例

第一步：创建starter工程spring-boot-cache-redis并配置pom.xml文件

~~~xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!-- 引入 jackson 对象json转换 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
    </dependency>

</dependencies>

~~~

第二步：创建application.xml

~~~yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
~~~

第三步：配置文件

~~~java
@Configuration
public class RedisConfig {

    /**
     * 默认情况下的模板只能支持RedisTemplate<String, String>，也就是只能存入字符串，因此支持序列化
     */
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

~~~

第五步：测试

~~~java
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheRedisApplication.class)
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Test
    public void redisUtilTest() {
        String key = "user-1";
        redisUtil.set(key, JSONObject.toJSONString(new User("张三", 22)));

    }

    @Test
    public void redisCacheTest() {
        String key = "user-2";
        redisCacheTemplate.opsForValue().set(key, new User("张三", 22));

        User user = (User) redisCacheTemplate.opsForValue().get(key);
        System.out.println(JSONObject.toJSONString(user));
    }
}

~~~

### [文档链接](https://www.jianshu.com/p/06534dca0a36)