package com.mamba.common.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";

    private IpUtil() {
    }

    /**
     * 获取客户端真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // 多次反向代理后取第一个IP
            int index = ip.indexOf(',');
            return index != -1 ? ip.substring(0, index).trim() : ip.trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        return StrUtil.blankToDefault(request.getRemoteAddr(), UNKNOWN);
    }

    /**
     * 判断IP是否有效
     */
    private static boolean isValidIp(String ip) {
        return StrUtil.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }

}
