## 整合短信相关

### 第一步：创建starter工程spring-boot-sms并配置pom.xml文件

```xml
<dependencies>
    <!-- 阿里云短信 -->
    <dependency>
        <groupId>com.aliyun</groupId>
        <artifactId>aliyun-java-sdk-core</artifactId>
        <version>4.1.0</version>
    </dependency>
    <!--腾讯短信-->
    <dependency>
        <groupId>com.tencentcloudapi</groupId>
        <artifactId>tencentcloud-sdk-java</artifactId>
        <version>3.1.390</version>
        <exclusions>
            <exclusion>
                <groupId>com.squareup.okio</groupId>
                <artifactId>okio</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <!-- google公司java工具包 -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
    </dependency>
</dependencies>

```

### 第二步： 配置文件

```yaml
ali:
  config:
    accessKeyId: LTAI4G6xynjeASQXXX
    accessKeySecret: cadE9VpjEsSUD1smalXXX
    sms:
      signName: \u963F\u91CC\u4E91\u77ED\u4FE1\u6D4B\u8BD5
      templateCode: SMS_154950909
logging:
  level:
    com.example: debug
```

### 第三步： 配置类

#### 1. 阿里

```java
package com.example.config.properties;

@Data
@Configuration
@ConfigurationProperties(prefix = "ali.config")
public class AliYunProperties {
    /**
     * 阿里云accessKeyId
     */
    private String accessKeyId;
    /**
     * 阿里云accessKeySecret
     */
    private String accessKeySecret;

    //指定该属性为嵌套值, 否则默认为简单值导致对象为空（外部类不存在该问题， 内部static需明确指定）
    @NestedConfigurationProperty
    private Sms sms;

    /**
     * 短信配置
     */
    @Data
    public static class Sms {
        /**
         * 短信服务签名
         */
        private String signName;
        /**
         * 短信模板code
         */
        private String templateCode;
    }
}

```

#### 2.腾讯

```java

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Configuration
//@ConfigurationProperties(prefix = "ali.config")
public class TencentSmsProperties {

    /**
     * api相关
     */
    private String url;
    private String region;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

    /**
     * 标识渠道商Id
     */
    private Integer supplierId;

    /**
     * 标识渠道商名字
     */
    private String supplierName;

}

```

### 第四步：业务层

#### 1. 注解（标记注入spring容器）

```java

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface SmsScriptHandler {
    /**
     * 这里输入脚本名
     */
    String value();
}


```

#### 2. 接口（规范）

```java
public interface SmsScript {

    public abstract void send(String phone, String captcha, SmsEnum smsEnum);
}

```

#### 3. 抽象类

```java

@Slf4j
public abstract class BaseSmsScript implements SmsScript {

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @PostConstruct
    public void registerProcessScript() {
        if (ArrayUtil.isEmpty(this.getClass().getAnnotations())) {
            log.error("BaseSmsScript can not find annotation!");
            return;
        }
        Annotation handlerAnnotations = null;
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof SmsScriptHandler) {
                handlerAnnotations = annotation;
                break;
            }
        }
        if (handlerAnnotations == null) {
            log.error("handler annotations not declared");
            return;
        }
        //注册handler
        smsScriptHolder.putHandler(((SmsScriptHandler) handlerAnnotations).value(), this);
    }
}

```

#### 4. 映射关系

```java

@Component
public class SmsScriptHolder {
    private Map<String, SmsScript> handlers = new HashMap<>(8);

    public void putHandler(String scriptName, SmsScript handler) {
        handlers.put(scriptName, handler);
    }

    public SmsScript route(String scriptName) {
        return handlers.get(scriptName);
    }
}
```

#### 5. 实现类

##### 5.1 阿里

```java
//@Service
@Slf4j
@SmsScriptHandler("AliSmsScript")
public class AliSmsServiceImpl extends BaseSmsScript {

    @Resource
    private AliYunProperties aliYunProperties;

    /**
     * 域地址
     */
    final static String DOMAIN = "dysmsapi.aliyuncs.com";
    /**
     * 版本号
     */
    final static String VERSION = "2017-05-25";


    /**
     * 发送短信验证码
     *  @param phone
     * @param captcha
     */
    @Override
    public void send(String phone, String captcha, SmsEnum smsEnum) {
        // 1.初始化
        IAcsClient client = getClient();
        // 2.组装发送短信参数
        CommonRequest request = init(phone, captcha, smsEnum);
        try {
            // 3.处理响应数据
            CommonResponse response = client.getCommonResponse(request);
            assembleSmsRecord(phone, response);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(request));
        }
    }

    private void assembleSmsRecord(String phone, CommonResponse response) {
        if (response == null) {
            return;
        }

        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(response));
        log.info("【短信】响应报文: {}", json);

        if (!StrUtil.equals(json.getJSONObject("data").getString("Code"), "OK")) {
            log.error("【短信】发送失败, data:{}", json);
            return;
        }

        SmsRecord smsRecord = SmsRecord.builder()
                .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                .phone(Long.valueOf(phone)).build();

    }

    /**
     * 组装参数
     *
     * @param phone
     * @param captcha
     * @param smsEnum
     * @return
     */
    private CommonRequest init(String phone, String captcha, SmsEnum smsEnum) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction("SendSms");
        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //签名 请在控制台签名管理页面签名名称一列查看。
        request.putQueryParameter("SignName", "阿里云短信测试");
        //模板
        request.putQueryParameter("TemplateCode", smsEnum.getValue());
        JSONObject templateCode = new JSONObject();
        templateCode.put("code", captcha);
        //模板参数,Json格式
        request.putQueryParameter("TemplateParam", templateCode.toJSONString());
        return request;
    }

    /**
     * 初始化客户端
     *
     * @return
     */
    private IAcsClient getClient() {
        DefaultProfile profile = DefaultProfile.getProfile("default", aliYunProperties.getAccessKeyId(), aliYunProperties.getAccessKeySecret());
        //赋值
        return new DefaultAcsClient(profile);
    }

}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 短信（回执和发送记录）
 */
class SmsRecord {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;

    /**
     * 短信发送的内容
     */
    private String msgContent;

    /**
     * 批次号Id
     */
    private String seriesId;

    /**
     * 计费条数
     */
    private Integer chargingNum;

    /**
     * 回执信息
     */
    private String reportContent;

    /**
     * 短信状态
     */
    private Integer status;

    /**
     * 发送日期
     */
    private Integer sendDate;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 更新时间
     */
    private Integer updated;
}


```

##### 5.2 腾讯

```java

@Slf4j
@SmsScriptHandler("TencentSmsScript")
public class TencentSmsScript extends BaseSmsScript {
    @Override
    public void send(String phone, String captcha, SmsEnum smsEnum) {
        try {
            log.info("【腾讯短信】发送!!");
            TencentSmsProperties tencentSmsProperties = new TencentSmsProperties();
            SmsClient client = init(tencentSmsProperties);
//            SendSmsRequest request = assembleReq(smsParam, tencentSmsProperties);
//            SendSmsResponse response = client.SendSms(request);
//             assembleSmsRecord(smsParam, response, tencentSmsProperties);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{}", Throwables.getStackTraceAsString(e));
        }
    }
}

```

### 第五步 测试

```java

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SmsTest {

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @Test
    public void scriptSend() {
        smsScriptHolder.route("TencentSmsScript").send("18806513872", "1212", SmsEnum.用户登录);

    }
}

```
