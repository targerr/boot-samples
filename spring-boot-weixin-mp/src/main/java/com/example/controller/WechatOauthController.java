package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date 2022/6/22 09:35
 * @Classname WechatOauthController
 * @Description 微信授权
 * doc: https://gitee.com/binary/weixin-java-tools/wikis/MP_OAuth2%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83
 */
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
