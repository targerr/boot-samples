package com.example.common;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import com.example.excetpion.NoAuthorization;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:00
 * @Classname JwtUtil
 * @Description
 */
public class JwtUtil {
    /**
     * 密钥 -- 根据实际项目，这里可以做成配置
     */
    public static final String KEY = "19921210";
    /**
     * 默认15天
     */
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 15;

    public static String createToken(User user) {
        Map<String, Object> map = new HashMap<String, Object>(2) {
            {
                put("user", user);
                put("expire_time", System.currentTimeMillis() + EXPIRE_TIME);
            }
        };
        final String token = JWTUtil.createToken(map, KEY.getBytes(StandardCharsets.UTF_8));

        return token;
    }

    public static boolean checkToken(HttpServletRequest request) throws NoAuthorization {
        String token = getToken(request);
        try {
            return JWTUtil.verify(token, KEY.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new NoAuthorization();
        }

    }

    public static  User parse(HttpServletRequest request) throws NoAuthorization {
        return  parse(getToken(request));
    }

    public static User parse(String token) throws NoAuthorization {
        if (token == null) {
            throw new NoAuthorization();
        }

        JWT user = null;
        try {
            user = JWTUtil.parseToken(token);
        } catch (Exception e) {
            throw new NoAuthorization();
        }

        return JSONObject.parseObject(JSONObject.toJSONString(user.getPayload("user")), User.class);
    }

    private static String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }
}
