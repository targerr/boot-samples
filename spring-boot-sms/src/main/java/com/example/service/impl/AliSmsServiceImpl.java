package com.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.example.annotation.SmsScriptHandler;
import com.example.config.properties.AliYunProperties;
import com.example.enums.SmsEnum;
import com.example.service.BaseSmsScript;
import com.example.service.SmsScript;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:55
 * @Classname SmsServiceImpl
 * @Description
 */
//@Service
@Slf4j
@SmsScriptHandler("AliSmsScript")
public class AliSmsServiceImpl extends BaseSmsScript  {

    @Resource
    private AliYunProperties aliYunProperties;

    /**
     * 域地址
     */
    final static String DOMAIN = "dysmsapi.aliyuncs.com";
    /**
     * 版本号
     */
    final static String VERSION = "2017-05-25";


    /**
     * 发送短信验证码
     *  @param phone
     * @param captcha
     */
    @Override
    public void send(String phone, String captcha, SmsEnum smsEnum) {
        // 1.初始化
        IAcsClient client = getClient();
        // 2.组装发送短信参数
        CommonRequest request = init(phone, captcha, smsEnum);
        try {
            // 3.处理响应数据
            CommonResponse response = client.getCommonResponse(request);
            assembleSmsRecord(phone, response);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(request));
        }
    }

    private void assembleSmsRecord(String phone, CommonResponse response) {
        if (response == null) {
            return;
        }

        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(response));
        log.info("【短信】响应报文: {}", json);

        if (!StrUtil.equals(json.getJSONObject("data").getString("Code"), "OK")) {
            log.error("【短信】发送失败, data:{}", json);
            return;
        }

        SmsRecord smsRecord = SmsRecord.builder()
                .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                .phone(Long.valueOf(phone)).build();

    }

    /**
     * 组装参数
     *
     * @param phone
     * @param captcha
     * @param smsEnum
     * @return
     */
    private CommonRequest init(String phone, String captcha, SmsEnum smsEnum) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction("SendSms");
        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //签名 请在控制台签名管理页面签名名称一列查看。
        request.putQueryParameter("SignName", "阿里云短信测试");
        //模板
        request.putQueryParameter("TemplateCode", smsEnum.getValue());
        JSONObject templateCode = new JSONObject();
        templateCode.put("code", captcha);
        //模板参数,Json格式
        request.putQueryParameter("TemplateParam", templateCode.toJSONString());
        return request;
    }

    /**
     * 初始化客户端
     *
     * @return
     */
    private IAcsClient getClient() {
        DefaultProfile profile = DefaultProfile.getProfile("default", aliYunProperties.getAccessKeyId(), aliYunProperties.getAccessKeySecret());
        //赋值
        return new DefaultAcsClient(profile);
    }

}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 短信（回执和发送记录）
 */
class SmsRecord {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;

    /**
     * 短信发送的内容
     */
    private String msgContent;

    /**
     * 批次号Id
     */
    private String seriesId;

    /**
     * 计费条数
     */
    private Integer chargingNum;

    /**
     * 回执信息
     */
    private String reportContent;

    /**
     * 短信状态
     */
    private Integer status;

    /**
     * 发送日期
     */
    private Integer sendDate;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 更新时间
     */
    private Integer updated;
}

