## 整合推送相关

### 第一步：创建starter工程spring-boot-push并配置pom.xml文件

```xml

<dependencies>
    <!--个推依赖-->
    <dependency>
        <groupId>com.getui.push</groupId>
        <artifactId>restful-sdk</artifactId>
        <version>1.0.0.6</version>
    </dependency>
    <!--极光推送依赖-->
    <dependency>
        <groupId>cn.jpush.api</groupId>
        <artifactId>jpush-client</artifactId>
        <version>3.6.0</version>
    </dependency>
</dependencies>

```

### 第二步： 配置文件

```yaml
push:
  # 个推配置
  getui:
    # 测试
    # 应用都对应一个唯一的AppID
    appId: 34w1ABjqE66S6bo8AYcqe6
    # 预先分配的第三方应用对应的Key
    appKey: IXVCKs6fT29bZ93Up4DzN5
    # 第三方客户端个推集成鉴权码
    appSecret:
    # 个推服务端API鉴权码
    masterSecret: 2rydS16GXv7w9lxKUBhoM3
  # 极光配置
  jiguang:
    # 应用都对应一个唯一的AppID
    appKey: 769873be50c4ac2af94a3785
    # 个推服务端API鉴权码
    masterSecret: 94f38872b2fe07594a72f495

logging:
  level:
    com.example: debug
```

### 第三步： 配置类

#### 1. 个推

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

#### 2.极光

```java

@Data
@ConfigurationProperties(prefix = "push.jiguang")
@Component
public class JpushProperties {
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

### 第四步：业务层

#### 1. 接口

```java
public interface PushSender {
    @Getter
    enum PushSenderEnum {
        /**
         * 个推
         */
        GE_TUI(),
        /**
         * 极光
         */
        JI_GUANG();
    }

    /**
     * 获取推送商户
     * @return
     */
    public abstract PushSenderEnum getPushSenderEnum();

    /**
     * 单推
     * @param sendRequest
     */
    public abstract void singleMsg(SendRequest sendRequest);
}

```

#### 2. 抽象类（模板方法）

```java
public abstract class BasePushSender implements PushSender{
    /**
     * 推送单条
     */
    @Override
    public void singleMsg(SendRequest sendRequest) {
        console(sendRequest);
        validate(sendRequest);
        execute(sendRequest);
    }

    /**
     * 验证消息
     * @param sendRequest
     */
    protected abstract void validate(SendRequest sendRequest);

    /**
     * 发送消息
     *
     * @param sendRequest
     */
    protected abstract void execute(SendRequest sendRequest);

    /**
     * 打印日志
     *
     * @param sendRequest
     */
    protected abstract void console(SendRequest sendRequest);


}

```

#### 3. 个推

```java
@Slf4j
@Service
public class GetuiService {
    @Autowired
    private PushApi pushApi;
    @Autowired
    private UserApi userApi;

    /**
     * cid 推送
     *
     * @param sendRequest
     */
    protected void pushToSingleByCid(SendRequest sendRequest) {
        PushDTO<Audience> pushDTO = pushDTO(sendRequest);
        fullCid(pushDTO, sendRequest.getCid());
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        log.debug("【个推】请求报文:{}", JSONObject.toJSONString(pushDTO, true));
        if (apiResult.isSuccess()) {
            // success
            log.debug("【个推】推送响应参数 data: {}", JSONObject.toJSONString(apiResult, true));
            return;
        }
        // failed
        log.debug("【个推】推送失败响应参数 data: {}", JSONObject.toJSONString(apiResult, true));
        throw new PushException(104, "【个推】推送失败响应" + JSONObject.toJSONString(apiResult));

    }

    /**
     * 别名推送
     *
     * @param sendRequest
     */
    public void pushToSingleByAlias(SendRequest sendRequest) {
        batchUnboundAlias(sendRequest.getCid(), sendRequest.getUserId());
        bindAlias(sendRequest.getCid(), sendRequest.getUserId());

        PushDTO<Audience> pushDTO = pushDTO(sendRequest);
        //fullAlias(pushDTO, sendRequest.getAlias());
        fullAlias(pushDTO, sendRequest.getUserId());
        // 别名单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByAlias(pushDTO);
        log.debug("【个推】别名推送 请求参数:{} 响应参数: {}", JSONObject.toJSONString(pushDTO, true), JSONObject.toJSONString(apiResult, true));
        if (apiResult.isSuccess()) {
            // success
        }
    }

    /**
     * 文档
     * https://docs.getui.com/getui/server/rest_v2/common_args/
     *
     * @param sendRequest
     * @return
     */
    private PushDTO<Audience> pushDTO(SendRequest sendRequest) {
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        /**** 设置个推通道参数 *****/
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        GTNotification notification = new GTNotification();
        notification.setTitle(sendRequest.getMessageParam().getTitle());
        notification.setBody(sendRequest.getMessageParam().getBody());
//        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_PAYLOAD.type);

        JSONObject json = new JSONObject();
        json.put("type", "1");
        json.put("title", sendRequest.getMessageParam().getTitle());
        json.put("msgId", "1501468370309742593");
        notification.setPayload(json.toJSONString());
        notification.setUrl("https://www.getui.com");
        notification.setChannelLevel("3");


        pushMessage.setNotification(notification);
        //pushMessage.setTransmission("透传消息测试");


        /**** 设置厂商相关参数 ****/
        PushChannel pushChannel = new PushChannel();
        pushDTO.setPushChannel(pushChannel);
        /*配置安卓厂商参数*/
        buildAndroid(pushChannel, sendRequest);
        /*设置ios厂商参数*/
        buildIos(pushChannel, sendRequest);

        return pushDTO;
    }

    private List<PushDTO<Audience>> pushDTOList(SendRequest sendRequest) {
        List<PushDTO<Audience>> audiences = new ArrayList<>();
        List<String> cids = Arrays.asList(sendRequest.getCid().split(",")).stream().collect(Collectors.toList());
        for (String cid : cids) {
            sendRequest.setCid(cid);
            final PushDTO<Audience> audiencePushDTO = this.pushDTO(sendRequest);
            fullAlias(audiencePushDTO, sendRequest.getUserId());
            audiences.add(audiencePushDTO);

            bindAlias(cid, sendRequest.getUserId());

        }
        return audiences;


    }

    private void buildIos(PushChannel pushChannel, SendRequest sendRequest) {
        IosDTO iosDTO = new IosDTO();
        pushChannel.setIos(iosDTO);
        // 相同的collapseId会覆盖之前的消息
        iosDTO.setApnsCollapseId(System.currentTimeMillis() + "");
        Aps aps = new Aps();
        iosDTO.setAps(aps);
        Alert alert = new Alert();
        aps.setAlert(alert);
        alert.setTitle(sendRequest.getMessageParam().getTitle());
        alert.setBody(sendRequest.getMessageParam().getBody());
    }

    private void buildAndroid(PushChannel pushChannel, SendRequest sendRequest) {
        AndroidDTO androidDTO = new AndroidDTO();
        pushChannel.setAndroid(androidDTO);
        Ups ups = new Ups();
//        ups.setTransmission("透传消息");

        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        thirdNotification.setTitle("title-" + System.currentTimeMillis());
        thirdNotification.setBody("content");
        ups.setNotification(thirdNotification);

        androidDTO.setUps(ups);

    }

    /**
     * 绑定别名
     *
     * @param cid
     * @param alias
     */
    public void bindAlias(String cid, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(cid, alias));
        ApiResult<Void> apiResult = userApi.bindAlias(cidAliasListDTO);
        log.debug("【个推】绑定别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 根据别名查询cid
     */
    public void queryCidByAlias(String alias) {
        ApiResult<QueryCidResDTO> apiResult = userApi.queryCidByAlias(alias);
        log.debug("【个推】根据别名查询 \n allias:{} data: {}", alias, JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 解绑别名
     *
     * @param cid
     * @param alias
     */
    public void batchUnboundAlias(String cid, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(cid, alias));
        ApiResult<Void> apiResult = userApi.batchUnbindAlias(cidAliasListDTO);
        log.debug("【个推】解绑别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    public void bindAlias(List<String> cids, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        if (cids != null && cids.isEmpty()) {
            cids.forEach(e -> cidAliasListDTO.add(new CidAliasListDTO.CidAlias(e, alias)));
        }
        ApiResult<Void> apiResult = userApi.bindAlias(cidAliasListDTO);
        log.debug("【个推】绑定别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 解绑别名
     *
     * @param alias
     */
    public void batchUnboundAlias(String alias) {
        ApiResult<Void> unbindAllAlias = userApi.unbindAllAlias(alias);

        log.debug("【个推】解绑别名 data: {}", unbindAllAlias);
    }

    private void fullCid(PushDTO<Audience> pushDTO, String cid) {
        Audience audience = new Audience();
        audience.addCid(cid);
        pushDTO.setAudience(audience);
    }

    private void fullAlias(PushDTO<Audience> pushDTO, String alias) {
        Audience audience = new Audience();
        audience.addAlias(alias);
        pushDTO.setAudience(audience);
    }

}

```
#### 4. 个推模板实现
```java
@Service
@Slf4j
public class GetuiSender extends BasePushSender {
    @Autowired
    private GetuiService getuiService;

    @Override
    public PushSenderEnum getPushSenderEnum() {
        return PushSenderEnum.GE_TUI;
    }

    @Override
    protected void validate(SendRequest sendRequest) {
        if (sendRequest == null || StrUtil.isEmpty(sendRequest.getCid())) {
            log.debug("【个推】推送参数不合法 data: {}", JSONObject.toJSONString(sendRequest, true));
            throw new PushException(104, "推送参数不合法");
        }
    }

    @Override
    protected void execute(SendRequest sendRequest) {
//        pushToSingleByCid(sendRequest);
        sendRequest.setUserId("123456");
        getuiService.pushToSingleByAlias(sendRequest);
    }

    @Override
    protected void console(SendRequest sendRequest) {
        log.debug("【个推】推送参数 data: {}", JSONObject.toJSONString(sendRequest, true));
    }

}

```

#### 5. 极光
```java
@Service
@Slf4j
public class JiGuangSender extends BasePushSender {
    @Autowired
    private JPushClient jPushClient;

    @Override
    public PushSenderEnum getPushSenderEnum() {
        return PushSenderEnum.JI_GUANG;
    }

    @Override
    protected void validate(SendRequest sendRequest) {
        if (sendRequest == null || StrUtil.isEmpty(sendRequest.getCid())) {
            log.debug("【极光】推送参数不合法 data: {}", JSONObject.toJSONString(sendRequest, true));
            throw new PushException(104, "【极光】推送参数不合法");
        }
    }

    @Override
    protected void execute(SendRequest sendRequest) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(sendRequest.getCid()))
                .setNotification(Notification.alert(sendRequest.getMessageParam().getBody()))
                .build();

        PushPayload.Builder builder = PushPayload.newBuilder();
//        if ("1".equals(sendRequest.getTokenType())) {
//            builder.setNotification(Notification.android(sendRequest.getMessageParam().getBody(), sendRequest.getMessageParam().getTitle(), null));
//        }
//        if ("0".equals(sendRequest.getTokenType())) {
//            builder.setNotification(Notification.ios(sendRequest.getMessageParam().getBody(), null));
//        }
        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("Got result - " + JSONObject.toJSONString(result, true));
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }
    }

    @Override
    protected void console(SendRequest sendRequest) {
        log.debug("【极光】推送参数 data: {}", JSONObject.toJSONString(sendRequest, true));
    }

}

```
#### 6. 映射关系

```java
@Slf4j
@Service
public class SenderActivity {
    private static final Map<PushSender.PushSenderEnum, PushSender> PUSH_SENDER_MAP = new ConcurrentHashMap<>(4);

    /**
     * 构造器注入 代替工厂模式
     *
     * @param pushSenderList
     */
    public SenderActivity(List<PushSender> pushSenderList) {
        for (PushSender pushSender : pushSenderList) {
            PUSH_SENDER_MAP.put(pushSender.getPushSenderEnum(), pushSender);
        }
    }

    /**
     * 获取对应的实现
     *
     * @param pushSenderEnum
     * @return
     */
    public PushSender getSender(PushSender.PushSenderEnum pushSenderEnum) {
        return PUSH_SENDER_MAP.get(pushSenderEnum);
    }

    /**
     * 发送消息
     * @param pushSenderEnum
     * @param sendRequest
     */
    public void singleMsg(PushSender.PushSenderEnum pushSenderEnum, SendRequest sendRequest) {
        PushSender pushSender = getSender(pushSenderEnum);
        pushSender.singleMsg(sendRequest);
    }
}

```

### 第五步 测试

```java

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PushTest {
    @Autowired
    private SenderActivity senderActivity;

    private static final String cid = "b227f948755e64597caca98a5a81f22a";

    @Test
    public void send() {
        SendRequest sendRequest = SendRequest.builder()
                .cid(cid)
                .messageParam(MessageParam.builder()
                        .title("测试头")
                        .body("极光推送")
                        .build())
                .build();

        // 个推
        senderActivity.singleMsg(PushSender.PushSenderEnum.GE_TUI, sendRequest);
        // 极光
        //  senderActivity.singleMsg(PushSender.PushSenderEnum.JI_GUANG, sendRequest);
    }
}


```
