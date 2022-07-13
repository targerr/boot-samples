package com.example.entity;

import cn.hutool.core.util.StrUtil;
import com.example.enums.PayTypeEnum;
import com.example.form.PaymentForm;
import com.ijpay.core.kit.IpKit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: wgs
 * @Date 2022/6/14 16:49
 * @Classname PayParam
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayParam {
    private String userId;
    private String orderNo;
    private String fee;
    /**
     * 不同的支付方式参数不一样（比如jsapi需要openId）
     */
    private PayReqModel payReqModel;

    public static PayParam buildParam(PaymentForm paymentForm, HttpServletRequest request, String attach) {
        String ip = StrUtil.isBlank(IpKit.getRealIp(request)) ? "127.0.0.1" : IpKit.getRealIp(request);
        PayTypeEnum payTypeEnum = PayTypeEnum.getPayType(paymentForm.getPayType());

        PayParam payParam = null;
        switch (payTypeEnum) {
            case WX_H5:
                payParam = PayParam.builder()
                        .payReqModel(WxH5Model.builder()
                                .body("幻鲲").attach(attach)
                                .ip(ip).build())
                        .build();
                break;
            case WX_JSAPI:
                payParam = PayParam.builder()
                        .payReqModel(WxJSAPIModel.builder()
                                .body("幻鲲").attach(attach).ip(ip)
                                .openId(paymentForm.getOpenId()).build())
                        .build();
            default:
        }

        return payParam;
    }
}
