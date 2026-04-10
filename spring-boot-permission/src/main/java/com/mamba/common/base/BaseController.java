package com.mamba.common.base;

import cn.dev33.satoken.stp.StpUtil;
import com.mamba.common.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础控制器
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获取当前登录用户名
     */
    protected String getCurrentUser() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 获取客户端IP
     */
    protected String getCurrentIp() {
        return IpUtil.getIpAddr(request);
    }

}
