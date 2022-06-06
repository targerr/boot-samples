package com.example.interceptor;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.JwtUtil;
import com.example.enums.ErrorTypes;
import com.example.excetpion.NoAuthorization;
import com.example.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wgs
 * @Date 2022/6/6 08:56
 * @Classname LongInterceptor
 * @Description
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoAuthorization {
        boolean trueToken = JwtUtil.checkToken(request);
        if (!trueToken){
            String returnUrl = request.getRequestURI();
            log.info("用户没有登录.IP:{},returnUrl:{},user-agent:{}", ServletUtil.getClientIP(request),returnUrl,request.getHeader("user-agent"));

            JSONObject json = new JSONObject();
            json.put("code",ErrorTypes.NO_LOGIN.getCode());
            json.put("msg",ErrorTypes.NO_LOGIN.getMsg());

            ResponseUtils.renderJson(response, json.toJSONString());
            return false;
        }
        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }
}
