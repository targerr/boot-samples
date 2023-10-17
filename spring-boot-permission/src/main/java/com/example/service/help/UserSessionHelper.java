package com.example.service.help;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wgs
 * @Date 2023/10/17 16:50
 * @Classname UserSessionHelper
 * @Description 使用jwt存储session, 则不需要后端存储session
 */
@Slf4j
@Component
public class UserSessionHelper {
    @Component
    @Data
    @ConfigurationProperties("per.jwt")
    public static class JwtProperties {
        /**
         * 签发人
         */
        private String issuer;
        /**
         * 密钥
         */
        private String secret;
        /**
         * 有效期，毫秒时间戳
         */
        private Long expire;
    }

    private final JwtProperties jwtProperties;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    public UserSessionHelper(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        verifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
    }

    public String genSession(Integer userId) {
        // 1.生成jwt格式的会话，内部持有有效期，用户信息
        JSONObject json = new JSONObject();
        json.put("s", RandomUtil.randomNumbers(10));
        json.put("u", userId);


        String session = json.toString();
        String token = JWT.create().withIssuer(jwtProperties.getIssuer()).withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpire()))
                .withPayload(session)
                .sign(algorithm);

        // 2.使用jwt生成token时，后端可以不存储这个session信息，可以完全依赖jwt的信息，
        // 但是考虑用户退出登录时，需要主动时效这个token,而jwt本身无状态，所以放到redis
        // 做简单token -> userId缓存，用于双重判定
//        RedisClient.setStrWithExpire(token, String.valueOf(userId), jwtProperties.getExpire() / 1000);

        return token;
    }


    public void removeSession(String session) {
//        RedisClient.del(session);
    }

    /**
     * 根据会话获取用户信息
     *
     * @param session
     * @return
     */
    public Long getUserIdBySession(String session) {
        // jwt的校验方式，如果token非法或者过期，则直接验签失败
        try {
            DecodedJWT decodedJWT = verifier.verify(session);
            String pay = new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
            // jwt验证通过，获取对应的userId
//            String userId = String.valueOf(JsonUtil.toObj(pay, HashMap.class).get("u"));
            String userId = JSONObject.parseObject(pay).getString("u");

            // 从redis中获取userId，解决用户登出，后台失效jwt token的问题
//            String user = RedisClient.getStr(session);
            String user = userId;
            if (user == null || !Objects.equals(userId, user)) {
                return null;
            }
            return Long.valueOf(user);
        } catch (Exception e) {
            log.error("jwt token校验失败! token: {}, msg: {}", session, e.getMessage());
            return null;
        }
    }

}
