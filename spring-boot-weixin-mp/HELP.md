## 整合微信公众号

### 示例

#### 第一步：创建starter工程spring-boot-weixinx-mp并配置pom.xml文件

~~~xml

<dependencies>
    <!--微信公众号开发工具包-->
    <dependency>
        <groupId>com.github.binarywang</groupId>
        <artifactId>wx-java-mp-spring-boot-starter</artifactId>
        <version>4.3.5.B</version>
    </dependency>
    <!--redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
~~~

#### 第二步：配置文件

```yaml
# 公众号配置(必填)
wx:
  mp:
    app-id: wx3eea24241a64xxxx
    secret: 875e35467082ed9e25da5f28004xxxx
    token:
    aesKey:
    config-storage:
      type: redistemplate
      key-prefix:
      redis:
        host: 127.0.0.1
        port: 6379
server:
  port: 80
```

#### 第三步: 控制类

```java
package com.example.controller;


@Controller
@RequestMapping("/weixin")
public class WechatOauthController {
    @Autowired
    private WxMpService mpService;

    @GetMapping("/weixinLogin")
    public String test(HttpServletRequest request) throws WxErrorException {
        // http://wanggss.nat300.top/weixin/weixinLogin
        String redirectUri = buildRedirectUri(request);
        final String snsapi_userinfo = mpService.getOAuth2Service().buildAuthorizationUrl(redirectUri, WxConsts.OAuth2Scope.SNSAPI_USERINFO, "snsapi_userinfo");

        System.out.println(snsapi_userinfo);
        return "redirect:" + snsapi_userinfo;

    }


    @GetMapping("/weixinLoginCallback")
    @ResponseBody
    public JSONObject weixinLoginCallback(HttpServletRequest request) throws WxErrorException {
        JSONObject json = new JSONObject();
        //用户同意授权，获取code
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        WxOAuth2AccessToken wxOAuth2AccessToken = mpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxMpUser = mpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, null);

        System.err.println(JSONObject.toJSONString(wxMpUser, true));
        if (StringUtils.isEmpty(code)) {
            json.put("msg", "授权失败! code不能为空!");
            return json;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(wxMpUser));
    }

    // 构建微信授权成功回调地址
    private String buildRedirectUri(HttpServletRequest request) {
        String siteUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        return siteUrl + "/weixin/weixinLoginCallback";
    }

    @GetMapping("/sendMsg")
    public String sendMessage(HttpServletRequest request) throws WxErrorException {


        final WxMpKefuMessage build = WxMpKefuMessage
                .TEXT()
                .toUser("oIzUz6BjAjA6OxhXikzGvV-yT5XA")
                .content("发送文字推送")
                .build();

        mpService.getKefuService().sendKefuMessage(build);
        return "ok";

    }
}


```
[参考文档](https://gitee.com/binary/weixin-java-tools/wikis/MP_OAuth2%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83)