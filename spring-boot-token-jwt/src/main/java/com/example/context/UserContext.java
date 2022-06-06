package com.example.context;

import cn.hutool.jwt.JWT;
import com.example.common.JwtUtil;
import com.example.entity.User;
import com.example.enums.ErrorTypes;
import com.example.excetpion.NoAuthorization;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:39
 * @Classname UserContext
 * @Description
 */
public class UserContext {
    private static final String S_USER = "s_user";

    /**
     * 获取当前登录用户
     * @param session
     * @return
     * @throws Exception
     */
    public static User getLoginUser(HttpServletRequest request) throws NoAuthorization {
        try {
            return JwtUtil.parse(request);
        } catch (NoAuthorization e) {
            throw new NoAuthorization();
        }
    }

    /**
     * 保存当前用户
     * @param session
     * @param user
     */
    public static void setLoginUser(HttpServletRequest request,User user) {
        request.getSession().setAttribute(S_USER, user);
    }
}
