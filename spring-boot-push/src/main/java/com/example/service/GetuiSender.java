package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.exception.PushException;
import com.example.param.SendRequest;
import com.example.service.getui.GetuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:36
 * @Classname GeTuiSender
 * @Description 个推推送
 */
@Service
@Slf4j
public class GetuiSender extends BasePushSender {
    @Autowired
    private GetuiService getuiService;

    @Override
    public PushSenderEnum getPushSenderEnum() {
        return PushSenderEnum.GE_TUI;
    }

    @Override
    protected void validate(SendRequest sendRequest) {
        if (sendRequest == null || StrUtil.isEmpty(sendRequest.getCid())) {
            log.debug("【个推】推送参数不合法 data: {}", JSONObject.toJSONString(sendRequest, true));
            throw new PushException(104, "推送参数不合法");
        }
    }

    @Override
    protected void execute(SendRequest sendRequest) {
//        pushToSingleByCid(sendRequest);
        sendRequest.setUserId("123456");
        getuiService.pushToSingleByAlias(sendRequest);
    }

    @Override
    protected void console(SendRequest sendRequest) {
        log.debug("【个推】推送参数 data: {}", JSONObject.toJSONString(sendRequest, true));
    }

}
