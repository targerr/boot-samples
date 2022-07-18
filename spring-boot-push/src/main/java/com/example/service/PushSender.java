package com.example.service;

import com.example.param.SendRequest;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:27
 * @Classname PushSender
 * @Description
 */
public interface PushSender {
    @Getter
    enum PushSenderEnum {
        /**
         * 个推
         */
        GE_TUI(),
        /**
         * 极光
         */
        JI_GUANG();
    }

    /**
     * 获取推送商户
     * @return
     */
    public abstract PushSenderEnum getPushSenderEnum();

    /**
     * 单推
     * @param sendRequest
     */
    public abstract void singleMsg(SendRequest sendRequest);
}
