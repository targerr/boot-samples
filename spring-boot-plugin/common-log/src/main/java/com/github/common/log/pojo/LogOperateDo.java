package com.github.common.log.pojo;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:24
 * @Classname LogOperateDo
 * @Description
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LogOperateDo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    protected LocalDateTime createdDate;
    /*** 备注 */
    protected String description;
    /**
     * 编号
     */
    private Long id;
    /**
     * 用户ID
     */
    private String username;
    /**
     * 日志标题
     */
    private String title;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private String operatorType;
    /**
     * 操作IP地址
     */
    private String ipAddress;
    /**
     * 登录地址
     */
    private String ipLocation;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 请求URI
     */
    private String requestUri;

    /** 异常详细 */
    /**
     * 操作方式
     */
    private String method;
    /**
     * 操作提交的数据
     */
    private String params;
    /**
     * 执行时间
     */
    private Long time;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 服务ID
     */
    private String serviceId;



    public void setDescription(String description) {
        if (description != null) {
            this.description = description.length() > 500 ? StrUtil.subPre(description, 500) : description;

        }
    }

}
