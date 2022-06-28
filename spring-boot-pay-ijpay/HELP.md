# 整合IJpay支付

## 第一步：创建starter工程spring-boot-pay-ijpay并配置pom.xml文件

```xml

<dependencies>
    <!--Ijpay微信支付依赖-->
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-WxPay</artifactId>
        <version>${ijapy.version}</version>
    </dependency>

</dependencies>

```

## 第二步： 配置文件

```yaml
# 微信支付配置
wxpay:
  # 应用编号
  appId: wx3eea2424xxx
  # 是appId对应的接口密码，微信公众号授权获取openID用
  appSecret: 875e35467082ed9e25daxxx
  # 微信支付商户号
  mchId: 1603227xxx
  # API 密钥
  partnerKey: a58dh75as34xxx
  # 证书路径，微信商户后台下载
  certPath: classpath:apiclient_cert.p1
  # 支付通知使用
  domain: https://wanggs.natapp4.cc/


v3:
  appId: 应用编号
  keyPath: key.pem
  certPath: cert.pem
  certP12Path: cert.p12
  platformCertPath: wx_cert.pem
  mchId: 微信支付商户号
  apiKey3: Api-v3
  apiKey: Api
  domain: https://wanggs.natapp4.cc/
```

## 第三步： 配置类

#### 1. v2(xml格式)版本

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="wxpay")
public class WxPayBean {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String certPath;
    private String domain;
}

```

#### 2. v3(json格式)版本
```java
package com.example.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "v3")
@Data
public class WxPayV3Bean {
    private String appId;
    private String keyPath;
    private String certPath;
    private String certP12Path;
    private String platformCertPath;
    private String mchId;
    private String apiKey;
    private String apiKey3;
    private String domain;
}
```

#### 3. 初始化配置（v2）
```java
package com.example.config;

@Configuration
public class WxPayConfig {
    @Autowired
    WxPayBean wxPayBean;

    @Bean
    public WxPayApiConfig wxPayApiConfig() {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder()
                .appId(wxPayBean.getAppId())
                .mchId(wxPayBean.getMchId())
                .partnerKey(wxPayBean.getPartnerKey())
                .certPath(wxPayBean.getCertPath())
                .domain(wxPayBean.getDomain())
                .build();
        // 放入缓存
        WxPayApiConfigKit.setThreadLocalWxPayApiConfig(wxPayApiConfig);
        return wxPayApiConfig;
    }

}

```

## 第四步：控制层
#### 1. v2 版本
```java
package com.example.controller.wxpay;


@Controller
@RequestMapping("/wxPay")
@Slf4j
public class WxPayController {
    @Autowired
    private WxPayBean wxPayBean;

    /**
     * 公众号支付
     */
    @RequestMapping(value = "/webPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject webPay(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        // openId，采用 网页授权获取
        String openId = "oIzUz6BjAjA6OxhXikzGvV-yT5XA";

        String ip = IpKit.getRealIp(request);
        if (StrUtil.isEmpty(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("测试-公众号支付")
                .attach("公众号测试联调")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1")
                .spbill_create_ip(ip)
                .notify_url(wxPayBean.getDomain().concat("/wxPay/payNotify"))
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayBean.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            json.put("code", returnCode);
            json.put("msg", returnMsg);
            return json;
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            json.put("code", returnCode);
            json.put("msg", returnMsg);
            return json;
        }

        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = resultMap.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayApiConfig.getAppId(), wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        json.put("code", "200");
        json.put("data", packageParams);
        return json;
    }


    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }
}

```

#### 2. v3 版本
```java
package com.example.controller.wxpay;

@Controller
@RequestMapping("/v3")
@Slf4j
public class WxPayV3Controller {
    private final static int OK = 200;

    @Resource
    WxPayV3Bean wxPayV3Bean;

    String serialNo;
    String platSerialNo;
    
    @RequestMapping("/jsApiPay")
    @ResponseBody
    public String jsApiPay(@RequestParam(value = "openId", required = false, defaultValue = "o-_-itxuXeGW3O1cxJ7FXNmq8Wf8") String openId) {
        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayV3Bean.getAppId())
                    .setMchid(wxPayV3Bean.getMchId())
                    .setDescription("支付测试")
                    .setOut_trade_no(PayKit.generateStr())
                    .setTime_expire(timeExpire)
                    .setAttach("微信系")
                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/payNotify"))
                    .setAmount(new Amount().setTotal(1))
                    .setPayer(new Payer().setOpenid(openId));

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.POST,
                    WxDomain.CHINA.toString(),
                    WxApiType.JS_API_PAY.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            log.info("统一下单响应 {}", response);

            if (response.getStatus() == OK) {
                // 根据证书序列号查询对应的证书来验证签名结果
                boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
                log.info("verifySignature: {}", verifySignature);
                if (verifySignature) {
                    String body = response.getBody();
                    JSONObject jsonObject = JSONUtil.parseObj(body);
                    String prepayId = jsonObject.getStr("prepay_id");
                    Map<String, String> map = WxPayKit.jsApiCreateSign(wxPayV3Bean.getAppId(), prepayId, wxPayV3Bean.getKeyPath());
                    log.info("唤起支付参数:{}", map);
                    return JSONUtil.toJsonStr(map);
                }
            }
            return JSONUtil.toJsonStr(response);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/payNotify", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");

            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("支付通知密文 {}", result);

            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wxPayV3Bean.getApiKey3(), wxPayV3Bean.getPlatformCertPath());

            log.info("支付通知明文 {}", plainText);

            if (StrUtil.isNotEmpty(plainText)) {
                response.setStatus(200);
                map.put("code", "SUCCESS");
                map.put("message", "SUCCESS");
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```