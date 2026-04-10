package com.example.datascope.security;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.provider.DataScopeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author wgs
 * @CreateTime: 2026-04-09
 * @Description: AOP获取
 * @Version 1.0
 */
@Component
@Slf4j
public class MyDataScopeProvider implements DataScopeProvider {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    public DataScope getDataScope() {
        DataScope dataScope = new DataScope();
        Set<Long> deptIds = new HashSet<>();
        deptIds.add(1L);
        deptIds.add(2L);
        deptIds.add(3L);
        dataScope.getDeptIds().addAll(deptIds);
        return dataScope;

    }
    // 从任意地方获取当前用户
//        User currentUser = getCurrentUser();
//        if (currentUser == null) {
//            return null;
//        }
//
//        // 构建数据权限对象
//        DataScope dataScope = new DataScope();
//
//        // 方式1：部门权限
//        dataScope.getDeptIds().addAll(currentUser.getDeptIds());
//
//        // 方式2：个人权限
//        // dataScope.setSelf(true);
//        // dataScope.setUserId(currentUser.getId());
//
//        // 方式3：全部权限
//        // dataScope.setAll(true);
//
//        return dataScope;
//    }
//
//    private User getCurrentUser() {
//        // TODO: 从你的认证方式中获取当前用户
//        // 例如：
//        // - JWT: 从 ThreadLocal 获取
//        // - Session: 从 HttpSession 获取
//        // - 自定义: 从你的上下文获取
//
//        return UserContext.getCurrentUser();
//    }
}
