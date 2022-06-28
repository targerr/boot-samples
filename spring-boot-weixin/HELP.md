# 整合微信公众号

## 第一步：创建starter工程spring-boot-weixin并配置pom.xml文件

```xml

<dependencies>
    <!--微信公众号、小程序-->
    <dependency>
        <groupId>net.dreamlu</groupId>
        <artifactId>mica-weixin</artifactId>
        <version>2.1.2</version>
    </dependency>
</dependencies>
```

## 第二步：配置文件

```yaml

wxpay:
  appId: 应用编号
  appsecret: 875e354670xxx
  token: 123

server:
  port: 80

dream:
  weixin:
    dev-mode: true
    url-patterns: /weixin/**
    app-id-key: appId
    wx-configs:
      - appId: wx3eea24241xx
        appSecret: 875e35467082ed9e25dxxx
        token: 123456
    json-type: jackson
```

## 第三步 配置类
```java
@Data
@Component
@ConfigurationProperties(prefix = "wxpay")
public class WxConfig {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String toke;
    private String certPath;
    private String domain;
    private String notifyUrl;

}
```

## 第四步：控制类
```java
package com.example.controller;


@WxMsgController("/weixin")
@Slf4j
public class WeixinController extends DreamMsgControllerAdapter {

	@Autowired
	private DreamWeixinProperties weixinProperties;

	@Resource
	private WxConfig wxConfig;


	@GetMapping("/weixinLogin")
	public String weixinLogin(HttpServletRequest request, HttpServletResponse response) {
		System.err.println(JSONObject.toJSONString(weixinProperties,true));
		// http://wanggss.nat300.top/weixin/weixinLogin
		String redirectUri = buildRedirectUri(request);
		String url = SnsAccessTokenApi.getAuthorizeURL(wxConfig.getAppId(), redirectUri, "", false);

		return "redirect:" + url;
	}


	@GetMapping("/weixinLoginCallback")
	@ResponseBody
	public JSONObject weixinLoginCallback(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		//用户同意授权，获取code
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		if (StringUtils.isEmpty(code)) {
			json.put("msg", "授权失败! code不能为空!");
			return json;
		}

		//通过code换取网页授权access_token
		SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(wxConfig.getAppId(), wxConfig.getAppSecret(), code);
		String token = snsAccessToken.getAccessToken();
		String openId = snsAccessToken.getOpenid();

		log.info("【微信授权】openId:{}", openId);
		//拉取用户信息(需scope为 snsapi_userinfo)
		ApiResult apiResult = SnsApi.getUserInfo(token, openId);
		// 异常情况
		if (apiResult.getErrorCode() != null) {
			log.error("【微信授权】授权失败,openId:{}", JSON.toJSONString(apiResult));
			json.put("msg", "授权失败!");
			return json;
		}
		if (apiResult.isSucceed()) {
			JSONObject jsonObject = JSON.parseObject(apiResult.getJson());
			log.info("【微信授权】,body:{}", jsonObject);
			return jsonObject;
		}

		return json;
	}

	// 构建微信授权成功回调地址
	private String buildRedirectUri(HttpServletRequest request) {
		String siteUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		return siteUrl + "/weixin/weixinLoginCallback";
	}


	@ResponseBody
	@GetMapping("/access")
	public String wxAccess(@RequestParam("signature") String signature,
						   @RequestParam("timestamp") String timestamp,
						   @RequestParam("nonce") String nonce,
						   @RequestParam("echostr") String echostr
	) {

		System.out.println("signature:" + signature);
		System.out.println("timestamp:" + timestamp);
		System.out.println("nonce:" + nonce);
		System.out.println("echostr:" + echostr);
		//直接return echostr 也是可以的

		return echostr;

	}

	@Override
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
		outMsg.setContent("关注消息~");
		render(outMsg);
	}

	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {
		System.out.println(JsonUtils.toJson(weixinProperties));
		OutTextMsg outMsg = new OutTextMsg(inTextMsg);
		outMsg.setContent(inTextMsg.getContent());
		render(outMsg);
	}

	@Override
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
		outMsg.setContent("菜单消息~");
		render(outMsg);
	}


}

```