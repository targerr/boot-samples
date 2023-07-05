package com.example.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2023/7/5 09:50
 * @Classname UserService
 * @Description
 */
public class UserService {

    private static Cache<String,User> cache = CacheBuilder.newBuilder()
            .maximumSize(100)//f放一百个数据
            .expireAfterAccess(10, TimeUnit.MINUTES)//天数
            .build();

    public User findById(Integer id) {
        User user = null;
        try {
            user = cache.get("user:" + id, new Callable<User>() {
                @Override
                public User call() {
                    // 查询用户信息
                   // return userDao.findById(id);
                    return  null;
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    class User{
        private String name;
    }
}
