package com.example.wxpay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.PayParam;
import com.example.entity.WxJSAPIModel;
import com.example.entity.WxPayBean;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/6/14 16:09
 * @Classname WxJsapi
 * @Description 微信公众号
 */
@Slf4j
@Service
public class WxJsapi extends WxpayPaymentService {
    @Resource
    private WxPayBean wxConfig;
//    @Resource
//    private UserService userService;

    @Override
    public JSONObject pay(PayParam data) {
        WxJSAPIModel req = (WxJSAPIModel) data.getPayReqModel();
        // openId
        String openId = StrUtil.isBlank(req.getOpenId()) ? getOpenId(data.getUserId()) : req.getOpenId();

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("幻鲲 支付")
                .attach(req.getAttach())
                .out_trade_no(data.getOrderNo())
                .total_fee(data.getFee())
                .spbill_create_ip(req.getIp())
                .notify_url(wxConfig.getNotifyUrl())
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("【微信公众号支付】响应报文,data:{}", xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            log.error("【微信公众号支付】异常,msg:{}", resultMap);
            return null;
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.error("【微信公众号支付】异常,msg:{}", returnMsg);
            return null;
        }

        String prepayId = resultMap.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayApiConfig.getAppId(), wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);


        return JSONObject.parseObject(JSON.toJSONString(packageParams));
    }

    public String getOpenId(String userId) {
        String openId = null;
//        String openId = userService.selectById(userId).getOpenId();
//        if (StringUtils.isBlank(openId)) {
//            throw new BizException("公众号支付,openId不能为空");
//        }
        return openId;
    }

}
