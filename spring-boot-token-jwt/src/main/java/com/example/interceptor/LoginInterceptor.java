package com.example.interceptor;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.annotation.PassToken;
import com.example.annotation.UserLoginToken;
import com.example.common.JwtUtil;
import com.example.enums.ErrorTypes;
import com.example.excetpion.NoAuthorization;
import com.example.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (!userLoginToken.required()) {
                return true;
            }
        }

        boolean trueToken = JwtUtil.checkToken(request);
        if (!trueToken) {
            String returnUrl = request.getRequestURI();
            log.info("用户没有登录.IP:{},returnUrl:{},user-agent:{}", ServletUtil.getClientIP(request), returnUrl, request.getHeader("user-agent"));

            JSONObject json = new JSONObject();
            json.put("code", ErrorTypes.NO_LOGIN.getCode());
            json.put("msg", ErrorTypes.NO_LOGIN.getMsg());

            ResponseUtils.renderJson(response, json.toJSONString());
            return false;
        }
        //TODO 数据库查询用户信息是否存在
        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }


    /**
     * 预处理
     * 判断是否要续期
     */
/*    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authToken = request.getHeader("Authorization");
        String token = authToken.substring("Bearer".length() + 1).trim();
        UserTokenDTO userTokenDTO = JWTUtil.parseToken(token);
        //1.判断请求是否有效

        String cacheToken = stringRedisTemplate.opsForValue().get(userTokenDTO.getId());
        if (StringUtils.isEmpty(cacheToken) || !cacheToken.equals(token)) {
            return false;
        }
        //2.判断是否需要续期
        if (stringRedisTemplate.getExpire(userTokenDTO.getId()) < expireTime) {
            stringRedisTemplate.opsForValue().set(userTokenDTO.getId(), token);
            log.error("update token info, id is:{}, user info is:{}", userTokenDTO.getId(), token);
        }
        return true;
    }*/
}
