package com.example.handler;

import com.example.domin.ParamInfo;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/7/29 11:23
 * @Classname DingDingHandler
 * @Description
 */
public interface DingDingHandler {
    @Getter
    enum DingDingEnum {
        DING_DING_ROBOT(80, "dingDingRobot(钉钉机器人)"),
        DING_DING_WORK_NOTICE(90, "dingDingWorkNotice(钉钉工作通知)"),
        ;
        /**
         * 编码值
         */
        private final Integer code;

        /**
         * 描述
         */
        private final String description;

        DingDingEnum(Integer code, String msg) {
            this.code = code;
            this.description = msg;
        }
    }

    /**
     * 映射用
     *
     * @return
     */
    public DingDingEnum getEnum();

    /**
     * 处理器
     *
     * @param paramInfo
     */
    public abstract boolean doHandler(ParamInfo paramInfo);

}
