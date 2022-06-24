package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.config.WxConfig;
import com.jfinal.kit.LogKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.weixin.annotation.WxMsgController;
import net.dreamlu.weixin.properties.DreamWeixinProperties;
import net.dreamlu.weixin.spring.DreamMsgControllerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
