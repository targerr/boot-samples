package com.example.controller;

import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.dto.UserTokenDTO;
import com.example.entity.vo.LoginUserVO;
import com.example.entity.vo.UpdatePasswordUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/6/9 09:06
 * @Classname UserController
 * @Description 测试续期相关
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 过期时间
    private final Long DURATION = 1 * 24 * 60 * 60 * 1000L;


    @PostMapping("/login")
    public String login(LoginUserVO loginUserVO) {
        //1.判断用户名密码是否正确 db操作 查询用户信息，比对密码

        //2.用户名密码正确生成token
        String userId = "1234";
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId(userId);
        userTokenDTO.setGmtCreate(System.currentTimeMillis());
        String token = "";//JWTUtil.generateToken(userTokenDTO);

        //3.存入token至redis
        redisTemplate.opsForValue().set(userId, token, DURATION, TimeUnit.MILLISECONDS);
        return token;
    }

    @PostMapping("/loginOut")
    public boolean loginOut(@RequestParam("id") String id) {
        return Boolean.TRUE.equals(redisTemplate.delete(id));
    }

    @PostMapping("/updatePassword")
    public String updatePassword(UpdatePasswordUserVO updatePasswordUserVO) {
        //1.修改密码 DB操作
        String userId = "66666";
        String userName = "tom";
        //2.生成新的token
        UserTokenDTO userTokenDTO = UserTokenDTO.builder()
                .id(userId)
                .userName(userName)
                .gmtCreate(System.currentTimeMillis()).build();
        String token = "";//JWTUtil.generateToken(userTokenDTO);
        //3.更新token
        redisTemplate.opsForValue().set(userId, JSONObject.toJSONString(userTokenDTO), DURATION, TimeUnit.MILLISECONDS);
        return token;
    }

}
