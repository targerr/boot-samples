# Spring Boot 字段级加密演示项目

## 🎯 项目概述

这是一个基于 Spring Boot 3 + MyBatis 的字段级加解密演示项目，实现了**透明**的字段级加密功能。通过简单的 `@Encrypted` 注解，即可实现敏感数据的自动加密存储和解密读取。

### ✨ 核心特性

- 🔐 **透明加密**：业务代码零侵入，自动加解密
- 🛡️ **安全算法**：使用 AES-GCM 加密算法，支持防篡改
- 🚀 **零配置**：注解驱动，开箱即用
- 🔧 **可扩展**：支持自定义加密算法和密钥管理

## 🚀 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+

### 2. 运行项目

```bash
# 克隆项目
git clone <repository-url>
cd springboot-column-encryption

# 编译运行
mvn spring-boot:run
```

### 3. 访问应用

- **前端界面**：http://localhost:8080
- **API接口**：http://localhost:8080/api/users
- **H2控制台**：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - 用户名: `sa`
  - 密码: `password`

## 📖 使用指南

### 基本用法

1. **在实体类字段上添加注解**：

```java
@Data
public class User {
    private Long id;
    private String username;

    @Encrypted  // 添加此注解即可实现自动加密
    private String phone;

    @Encrypted
    private String idCard;

    // 普通字段不会加密
    private Integer age;
}
```

2. **正常使用 MyBatis 操作**：

```java
// 插入数据 - 自动加密敏感字段
User user = new User();
user.setUsername("张三");
user.setPhone("13812345678");  // 会自动加密存储
user.setIdCard("110101199001011234");  // 会自动加密存储
userMapper.insert(user);

// 查询数据 - 自动解密敏感字段
User result = userMapper.findById(user.getId());
System.out.println(result.getPhone());  // 输出: 13812345678 (已自动解密)
```


### 4. 核心实现
1. ** 定义注解 **：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public      @interface      Encrypted {
              // 是否支持模糊查询
              boolean      supportFuzzyQuery()      default      false;
}

```
使用
```java
public      class      User      {
              private      Long id;
              private      String username;

              @Encrypted       // 这个字段会自动加密
              private      String phone;

              @Encrypted       // 这个字段也会自动加密
              private      String email;
}

```
2. ** 加密工具  **

```java
public class CryptoUtil {
  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int IV_LENGTH = 12;

  public static String encrypt(String plaintext) {
    // 生成随机IV
    byte[] iv = new byte[IV_LENGTH];
    new SecureRandom().nextBytes(iv);

    // 加密
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
    byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

    // 组合IV和密文，Base64编码
    byte[] encryptedData = new byte[iv.length + ciphertext.length];
    System.arraycopy(iv, 0, encryptedData, 0, iv.length);
    System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);

    return Base64.getEncoder().encodeToString(encryptedData);
  }

  public static String decrypt(String encryptedText) {
    // Base64解码
    byte[] encryptedData = Base64.getDecoder().decode(encryptedText);

    // 提取IV和密文
    byte[] iv = Arrays.copyOfRange(encryptedData, 0, IV_LENGTH);
    byte[] ciphertext = Arrays.copyOfRange(encryptedData, IV_LENGTH, encryptedData.length);

    // 解密
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
    byte[] plaintext = cipher.doFinal(ciphertext);

    return new String(plaintext);
  }

  // 检查是否已经加密，避免重复加密
  public static boolean isEncrypted(String value) {
    return value != null && value.contains(":");
  }
}


```

3. ** 拦截器 **

```java
加密后的格式：

Base64(IV):

Base64(密文)3.
MyBatis 拦截器拦截器是整个方案的核心，负责自动加解密：

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class EncryptionInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    String methodName = invocation.getMethod().getName();

    // UPDATE/INSERT 操作：加密输入参数
    if ("update".equals(methodName)) {
      Object parameter = getParameter(invocation);
      if (shouldEncrypt(parameter)) {
        encryptFields(parameter);
      }
    }

    // 执行原始SQL
    Object result = invocation.proceed();

    // SELECT 操作：解密查询结果
    if ("query".equals(methodName)) {
      decryptResult(result);
    }

    return result;
  }

  private void encryptFields(Object obj) {
    if (obj == null) return;

    // 只处理实体对象，不处理基本类型和Map
    if (isBasicType(obj.getClass()) || obj instanceof Map || obj instanceof Collection) {
      return;
    }

    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      if (field.isAnnotationPresent(Encrypted.class)) {
        try {
          field.setAccessible(true);
          Object value = field.get(obj);

          if (value instanceof String && !isEncrypted((String) value)) {
            String encrypted = CryptoUtil.encrypt((String) value);
            field.set(obj, encrypted);
          }
        } catch (Exception e) {
          log.error("加密字段失败: {}", field.getName(), e);
        }
      }
    }
  }

  private void decryptResult(Object result) {
    if (result instanceof List) {
      for (Object item : (List<?>) result) {
        decryptFields(item);
      }
    } else if (result != null) {
      decryptFields(result);
    }
  }

  private void decryptFields(Object obj) {
    // 解密逻辑和加密类似，但是反向操作
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      if (field.isAnnotationPresent(Encrypted.class)) {
        try {
          field.setAccessible(true);
          Object value = field.get(obj);

          if (value instanceof String && isEncrypted((String) value)) {
            String decrypted = CryptoUtil.decrypt((String) value);
            field.set(obj, decrypted);
          }
        } catch (Exception e) {
          log.error("解密字段失败: {}", field.getName(), e);
        }
      }
    }
  }
} 
```
4. ** 自动配置 **

```java
4.自动配置配置拦截器自动生效：

@Configuration
@ConditionalOnProperty(name = "encryption.enabled", havingValue = "true", matchIfMissing = true)
public class EncryptionAutoConfiguration {

  @Bean
  public ConfigurationCustomizer encryptionConfigurationCustomizer() {
    return configuration -> {
      configuration.addInterceptor(new EncryptionInterceptor());
    };
  }
}

只需要在配置文件中启用：encryption:
enabled:true使用效果数据库存储情况原始数据：用户信息：
姓名:张三 手机:13812345678
邮箱:zhangsan@example.com 身份证:110101199001011234数据库存储（自动加密后）：用户信息：
姓名:张三 手机:nTuVgMWime1:hFGa9as6JHxLT2vG8dpiRmu4wtxDnkTEr/1x 邮箱:mK7pL9xQ2rS8vN3w:jKxL9mN2pQ7rS8vT3wX4yZ6aB8cD1eF2g 身份证:X1Y2Z3A4B5C6D7E8:F9G0H1I2J3K4L5M6N7O8P9Q0R1S2T3U4V业务代码使用@Service

public class UserService {

  @Autowired
  private UserMapper userMapper;

  // 插入用户：自动加密存储
  public void createUser(User user) {
    // 这些字段会被拦截器自动加密
    user.setPhone("13812345678");
    user.setEmail("zhangsan@example.com");
    userMapper.insert(user);
  }

  // 查询用户：自动解密返回
  public User getUser(Long id) {
    User user = userMapper.findById(id);
    // 这些字段已经被拦截器自动解密
    System.out.println(user.getPhone());       // 13812345678
    System.out.println(user.getEmail());       // zhangsan@example.com
    return user;
  }
}

```

1. 密钥管理
```java

@Configuration
public class EncryptionConfig {

  @Value("${encryption.key}")
  private String encryptionKey;

  @Bean
  public SecretKey getSecretKey() {
    // 可以从环境变量、配置中心、密钥管理系统获取
    byte[] keyBytes = Base64.getDecoder().decode(encryptionKey);
    return new SecretKeySpec(keyBytes, "AES");
  }
}
```
2.日志安全避免在日志中打印敏感信息：
```java



public class SensitiveDataLogger {

  public String maskPhone(String phone) {
    if (phone == null || phone.length() != 11) return phone;
    return phone.substring(0, 3) + "****" + phone.substring(7);
  }

  public String maskEmail(String email) {
    if (email == null) return email;
    int atIndex = email.indexOf("@");
    if (atIndex <= 2) return "***" + email.substring(atIndex);
    return email.substring(0, 2) + "***" + email.substring(atIndex);
  }
}

```