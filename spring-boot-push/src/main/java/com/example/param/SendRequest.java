package com.example.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/1/20 11:20
 * @Classname SendRequest
 * @Description
 */
@Data
@Builder
public class SendRequest {
    /**
     * 执行业务类型
     */
    private String code;

    /**
     * 设备标识
     */
    private String cid;

    /**
     * 多个设备
     */
    private List<String> cidList;

    /**
     * 别名
     */
    private String alias;

    /**
     * token类型
     * 0:ios，1:Android
     */
    private Integer tokenType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 消息相关的参数
     */
    private MessageParam messageParam;
}
