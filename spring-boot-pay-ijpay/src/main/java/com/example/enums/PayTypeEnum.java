package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author: wgs
 * @Date 2022/6/14 15:50
 * @Classname PayEnmu
 * @Description
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum PayTypeEnum {
    ALI_BAR(1,"AliBar","支付宝条码支付"),
    ALI_JSAPI(2,"AliJsapi","支付宝服务窗支付"),
    ALI_APP(3,"AliApp","支付宝 app支付"),
    ALI_PC(4,"AliPc","支付宝 电脑网站支付"),
    ALI_WAP(5,"AliWap","支付宝 wap支付"),
    ALI_QR(6,"AiiQr","支付宝 二维码付款"),
    WX_JSAPI(7,"WxJsapi","微信jsapi支付"),
    WX_LITE(8,"WxLite","微信小程序支付"),
    WX_BAR(9,"WxBar","微信条码支付"),
    WX_H5(10,"WxH5","微信H5支付"),
    WX_NATIVE(11,"WxNative","微信扫码支付"),
    ;
    private Integer code;
    private String payCode;
    private String message;

    public static boolean checkPayType(String type){
        return Arrays.stream(PayTypeEnum.values()).anyMatch(e->e.getPayCode().equals(type));
    }

    public static PayTypeEnum getPayType(String type){
        return Arrays.stream(PayTypeEnum.values()).filter(e->e.getPayCode().equals(type)).findFirst().orElse(null);
    }

}
