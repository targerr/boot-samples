## 整合钉钉相关

### 第一步：创建starter工程spring-robot并配置pom.xml文件

```xml

<dependencies>
    <!--阿里云 钉钉 SDK-->
    <dependency>
        <groupId>com.aliyun</groupId>
        <artifactId>alibaba-dingtalk-service-sdk</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步： 配置文件

```yaml

logging:
  level:
    com.example: debug
```

### 第三步： 配置类

#### 1. 钉钉机器人

```java
@Component
@ConfigurationProperties(prefix = "dingding")
@Data
public class DingDingRobotProperties {
    /**
     * 密钥
     */
    private String secret;
    /**
     * 自定义群机器人中的 webhook
     */
    private String url;
}

```

#### 2.

```java

```

### 第四步：业务层

#### 1. 接口

```java
package com.example.handler;

import com.example.domin.ParamInfo;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/7/29 11:23
 * @Classname DingDingHandler
 * @Description
 */
public interface DingDingHandler {
    @Getter
    enum DingDingEnum {
        DING_DING_ROBOT(80, "dingDingRobot(钉钉机器人)"),
        DING_DING_WORK_NOTICE(90, "dingDingWorkNotice(钉钉工作通知)"),
        ;
        /**
         * 编码值
         */
        private final Integer code;

        /**
         * 描述
         */
        private final String description;

        DingDingEnum(Integer code, String msg) {
            this.code = code;
            this.description = msg;
        }
    }

    /**
     * 映射用
     *
     * @return
     */
    public DingDingEnum getEnum();

    /**
     * 处理器
     *
     * @param paramInfo
     */
    public abstract boolean doHandler(ParamInfo paramInfo);

}


```


#### 2. 钉钉机器人

```java
@Service
@Slf4j
public class DingDingRobotHandler implements DingDingHandler {
    @Autowired
    private DingDingRobotProperties dingDingRobotProperties;

    @Override
    public DingDingEnum getEnum() {
        return DingDingEnum.DING_DING_ROBOT;
    }

    @Override
    public boolean doHandler(ParamInfo paramInfo) {
        try {
            DingDingRobotParam dingDingRobotParam = assembleParam(paramInfo);
            String httpResult = HttpUtil.post(assembleParamUrl(), JSON.toJSONString(dingDingRobotParam));

            DingDingRobotResult dingDingRobotResult = JSON.parseObject(httpResult, DingDingRobotResult.class);
            if (dingDingRobotResult.getErrCode() == 0) {
                return true;
            }
            // 失败原因
            log.error("DingDingHandler#handler fail!result:{},params:{}", JSON.toJSONString(dingDingRobotResult), JSON.toJSONString(paramInfo));
        } catch (Exception e) {
            log.error("DingDingHandler#handler fail!e:{},params:{}", e.getMessage(), JSON.toJSONString(paramInfo));
        }
        return false;
    }


    private DingDingRobotParam assembleParam(ParamInfo paramInfo) {
        List<String> mobiles = new ArrayList<>();
        // 接受者
        DingDingRobotParam.AtVO atVo = DingDingRobotParam.AtVO.builder().build();
        if (DingDingConstant.SEND_ALL.equals(CollUtil.getFirst(paramInfo.getReceiver()))) {
            atVo.setIsAtAll(true);
        } else {
            atVo.setIsAtAll(false);
            mobiles.addAll(paramInfo.getReceiver());
            atVo.setAtMobiles(mobiles);
        }

        // 消息类型以及内容
        DingDingRobotParam param = DingDingRobotParam.builder().at(atVo)
                .msgtype(SendMessageType.getDingDingRobotTypeByCode(paramInfo.getSendType()))
                .build();

        if (SendMessageType.TEXT.getCode().equals(paramInfo.getSendType())) {
            param.setText(DingDingRobotParam.TextVO.builder().content(paramInfo.getContent()).build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(paramInfo.getSendType())) {
            // @指定人
            String content = buildMobileContent(paramInfo, mobiles);

            param.setMarkdown(DingDingRobotParam
                    .MarkdownVO.builder()
                    .title(paramInfo.getTitle())
                    .text(content)
                    .build());
        }
        if (SendMessageType.LINK.getCode().equals(paramInfo.getSendType())) {
            param.setLink(DingDingRobotParam.LinkVO.builder().title(paramInfo.getTitle()).text(paramInfo.getContent()).messageUrl(paramInfo.getUrl()).picUrl(paramInfo.getPicUrl()).build());
        }
        if (SendMessageType.NEWS.getCode().equals(paramInfo.getSendType())) {
            List<DingDingRobotParam.FeedCardVO.LinksVO> linksVOS = JSON.parseArray(paramInfo.getFeedCards(), DingDingRobotParam.FeedCardVO.LinksVO.class);
            DingDingRobotParam.FeedCardVO feedCardVO = DingDingRobotParam.FeedCardVO.builder().links(linksVOS).build();
            param.setFeedCard(feedCardVO);
        }

        return param;
    }

    private String buildMobileContent(ParamInfo paramInfo, List<String> mobiles) {
        String content = paramInfo.getContent();
        if (CollUtil.isNotEmpty(mobiles)) {
            content = "@" + String.join("@", mobiles) + content;
        }
        return content;
    }

    /**
     * 拼装 url
     *
     * @return
     */
    private String assembleParamUrl() {
        long currentTimeMillis = System.currentTimeMillis();
        String sign = buildSign(currentTimeMillis, dingDingRobotProperties.getSecret());
        return (dingDingRobotProperties.getUrl() + "&timestamp=" + currentTimeMillis + "&sign=" + sign);
    }

    /**
     * 使用HmacSHA256算法计算签名
     *
     * @param currentTimeMillis
     * @param secret
     * @return
     */
    private String buildSign(long currentTimeMillis, String secret) {
        String sign = "";
        try {
            String stringToSign = currentTimeMillis + String.valueOf(StrUtil.C_LF) + secret;
            Mac mac = Mac.getInstance(DingDingConstant.HMAC_SHA256_ENCRYPTION_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(DingDingConstant.CHARSET_NAME), DingDingConstant.HMAC_SHA256_ENCRYPTION_ALGO));
            byte[] signData = mac.doFinal(stringToSign.getBytes(DingDingConstant.CHARSET_NAME));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), DingDingConstant.CHARSET_NAME);
        } catch (Exception e) {
            log.error("DingDingHandler#assembleSign fail!:{}", e.getMessage());
        }
        return sign;
    }

}

```

#### 

```java

```

#### 6. 映射关系

```java
@Slf4j
@Service
public class DingDingActivity {

    public static final Map<DingDingHandler.DingDingEnum, DingDingHandler> MAP = new HashMap<>(4);

    public DingDingActivity(List<DingDingHandler> handlerList) {
        handlerList.forEach(e -> MAP.put(e.getEnum(), e));
    }

    public DingDingHandler getHandler(DingDingHandler.DingDingEnum dingDingEnum) {
        return MAP.get(dingDingEnum);
    }

    public boolean doHandler(DingDingHandler.DingDingEnum dingDingEnum, ParamInfo paramInfo) {
        return getHandler(dingDingEnum).doHandler(paramInfo);
    }
}

```

### 第五步 测试

```java
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class DingDingTest {
    @Autowired
    private DingDingActivity dingDingActivity;

    @Test
    public void robotTest() {
        log.info("【钉钉机器人】 发送中~~~~~");
        dingDingActivity.doHandler(DingDingHandler.DingDingEnum.DING_DING_ROBOT, new ParamInfo());
        log.info("【钉钉机器人】 发送结束");
    }
}


```

[钉钉demo](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)

