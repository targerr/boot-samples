package com.example.service;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2023/7/5 09:50
 * @Classname UserService
 * @Description
 */
@Data
@Slf4j
public class UserService {

    private static Cache<String, User> cache = CacheBuilder.newBuilder()
            .maximumSize(100)//放一百个数据
            .expireAfterAccess(10, TimeUnit.SECONDS)//过期时间
            .build();

    public User findById(Integer id) {
        User user = null;
        try {
            user = cache.get("user:" + id, new Callable<User>() {
                @Override
                public User call() {
                    // 查询用户信息
                    // return userDao.findById(id);
                    return findUserById(id);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User findUserById(Integer id) {
        log.info("模拟数据查询用户信息~~~");
        User user = new User();
        user.setId(id);
        user.setName("tom");
        return user;
    }

    @Data
    class User {
        private String name;
        private Integer id;
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        final User user = userService.findById(1);
        log.info(JSON.toJSONString(user, true));

        ThreadUtil.sleep(2000L);
     //  cache.invalidateAll();

        User user1 = userService.findById(1);

        log.info(JSON.toJSONString(user1, true));


    }
}
