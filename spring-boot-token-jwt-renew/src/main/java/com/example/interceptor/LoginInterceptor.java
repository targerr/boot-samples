package com.example.interceptor;

import com.example.entity.dto.UserTokenDTO;
import com.example.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static final int expireTime = 1 * 60 * 30;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
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
    }

}
