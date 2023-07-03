package com.example.filter;

import com.example.constant.CommonConstant;
import com.example.util.TokenParseUtil;
import com.example.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:11
 * @Classname LoginUserInfoInterceptor
 * @Description 用户信息统一拦截器
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 部分请求不需要携带用户信息。即白名单
        String uri = request.getRequestURI();
        if (checkWhiteListUrl(uri)) {
            return true;
        }

        // 尝试从 header 获取 token
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);

        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
        }

        if (loginUserInfo == null){
            throw  new RuntimeException("用户信息有误!");
        }

        log.info("set login user info: [{}]", request.getRequestURI());
        // 设置当前请求上下文, 把用户信息填充进去
        AccessContext.setUserInfoThreadLocal(loginUserInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 请求完全结束后调用，常用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessContext.clearLoginUserInfo();
    }

    /**
     * 校验白名单（swagger2）
     *
     * @param url
     * @return
     */
    private boolean checkWhiteListUrl(String url) {
        return StringUtils.containsAny(url,
                "springfox", "swagger", "v2",
                "webjars", "doc.html");
    }
}
