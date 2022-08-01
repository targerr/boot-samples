package com.example.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.example.config.DingDingRobotProperties;
import com.example.constant.DingDingConstant;
import com.example.domin.ParamInfo;
import com.example.enums.SendMessageType;
import com.example.handler.DingDingHandler;
import com.example.param.DingDingRobotParam;
import com.example.param.DingDingRobotResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/7/29 13:50
 * @Classname DingDingRobotHandler
 * @Description
 */
@Service
@Slf4j
public class DingDingRobotHandler implements DingDingHandler {
    @Autowired
    private DingDingRobotProperties dingDingRobotProperties;

    @Override
    public DingDingEnum getEnum() {
        return DingDingEnum.DING_DING_ROBOT;
    }

    @Override
    public boolean doHandler(ParamInfo paramInfo) {
        try {
            DingDingRobotParam dingDingRobotParam = assembleParam(paramInfo);
            String httpResult = HttpUtil.post(assembleParamUrl(), JSON.toJSONString(dingDingRobotParam));

            DingDingRobotResult dingDingRobotResult = JSON.parseObject(httpResult, DingDingRobotResult.class);
            if (dingDingRobotResult.getErrCode() == 0) {
                return true;
            }
            // 失败原因
            log.error("DingDingHandler#handler fail!result:{},params:{}", JSON.toJSONString(dingDingRobotResult), JSON.toJSONString(paramInfo));
        } catch (Exception e) {
            log.error("DingDingHandler#handler fail!e:{},params:{}", e.getMessage(), JSON.toJSONString(paramInfo));
        }
        return false;
    }


    private DingDingRobotParam assembleParam(ParamInfo paramInfo) {
        List<String> mobiles = new ArrayList<>();
        // 接受者
        DingDingRobotParam.AtVO atVo = DingDingRobotParam.AtVO.builder().build();
        if (DingDingConstant.SEND_ALL.equals(CollUtil.getFirst(paramInfo.getReceiver()))) {
            atVo.setIsAtAll(true);
        } else {
            atVo.setIsAtAll(false);
            mobiles.addAll(paramInfo.getReceiver());
            atVo.setAtMobiles(mobiles);
        }

        // 消息类型以及内容
        DingDingRobotParam param = DingDingRobotParam.builder().at(atVo)
                .msgtype(SendMessageType.getDingDingRobotTypeByCode(paramInfo.getSendType()))
                .build();

        if (SendMessageType.TEXT.getCode().equals(paramInfo.getSendType())) {
            param.setText(DingDingRobotParam.TextVO.builder().content(paramInfo.getContent()).build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(paramInfo.getSendType())) {
            // @指定人
            String content = buildMobileContent(paramInfo, mobiles);

            param.setMarkdown(DingDingRobotParam
                    .MarkdownVO.builder()
                    .title(paramInfo.getTitle())
                    .text(content)
                    .build());
        }
        if (SendMessageType.LINK.getCode().equals(paramInfo.getSendType())) {
            param.setLink(DingDingRobotParam.LinkVO.builder().title(paramInfo.getTitle()).text(paramInfo.getContent()).messageUrl(paramInfo.getUrl()).picUrl(paramInfo.getPicUrl()).build());
        }
        if (SendMessageType.NEWS.getCode().equals(paramInfo.getSendType())) {
            List<DingDingRobotParam.FeedCardVO.LinksVO> linksVOS = JSON.parseArray(paramInfo.getFeedCards(), DingDingRobotParam.FeedCardVO.LinksVO.class);
            DingDingRobotParam.FeedCardVO feedCardVO = DingDingRobotParam.FeedCardVO.builder().links(linksVOS).build();
            param.setFeedCard(feedCardVO);
        }

        return param;
    }

    private String buildMobileContent(ParamInfo paramInfo, List<String> mobiles) {
        String content = paramInfo.getContent();
        if (CollUtil.isNotEmpty(mobiles)) {
            content = "@" + String.join("@", mobiles) + content;
        }
        return content;
    }

    /**
     * 拼装 url
     *
     * @return
     */
    private String assembleParamUrl() {
        long currentTimeMillis = System.currentTimeMillis();
        String sign = buildSign(currentTimeMillis, dingDingRobotProperties.getSecret());
        return (dingDingRobotProperties.getUrl() + "&timestamp=" + currentTimeMillis + "&sign=" + sign);
    }

    /**
     * 使用HmacSHA256算法计算签名
     *
     * @param currentTimeMillis
     * @param secret
     * @return
     */
    private String buildSign(long currentTimeMillis, String secret) {
        String sign = "";
        try {
            String stringToSign = currentTimeMillis + String.valueOf(StrUtil.C_LF) + secret;
            Mac mac = Mac.getInstance(DingDingConstant.HMAC_SHA256_ENCRYPTION_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(DingDingConstant.CHARSET_NAME), DingDingConstant.HMAC_SHA256_ENCRYPTION_ALGO));
            byte[] signData = mac.doFinal(stringToSign.getBytes(DingDingConstant.CHARSET_NAME));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), DingDingConstant.CHARSET_NAME);
        } catch (Exception e) {
            log.error("DingDingHandler#assembleSign fail!:{}", e.getMessage());
        }
        return sign;
    }

}
