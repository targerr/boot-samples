package com.example.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.H5SceneInfo;
import com.example.entity.PayParam;
import com.example.entity.WxH5Model;
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
 * @Classname WxH5
 * @Description
 */
@Service
@Slf4j
public class WxH5 extends WxpayPaymentService {
    @Resource
    private WxPayBean wxConfig;

    @Override
    public JSONObject pay(PayParam data) {

        WxH5Model req = (WxH5Model) data.getPayReqModel();

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();


        H5SceneInfo sceneInfo = new H5SceneInfo();
        H5SceneInfo.H5 h5_info = new H5SceneInfo.H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        h5_info.setWap_url("https://gitee.com/javen205/IJPay");
        h5_info.setWap_name("幻鲲 支付");
        sceneInfo.setH5_info(h5_info);


        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(req.getBody())
                .attach(req.getAttach())
                .out_trade_no(data.getOrderNo())
                .total_fee(data.getFee())
                .spbill_create_ip(req.getIp())
                .notify_url(wxConfig.getNotifyUrl())
                .trade_type(TradeType.MWEB.getTradeType())
                .scene_info(JSON.toJSONString(sceneInfo))
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(return_code)) {
            throw new RuntimeException(return_msg);
        }
        String result_code = result.get("result_code");
        if (!WxPayKit.codeIsOk(result_code)) {
            throw new RuntimeException(return_msg);
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");

        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);

        JSONObject json = new JSONObject();
        json.put("mweb_url", webUrl);
        json.put("prepay_id", prepayId);
        return json;
    }
}
