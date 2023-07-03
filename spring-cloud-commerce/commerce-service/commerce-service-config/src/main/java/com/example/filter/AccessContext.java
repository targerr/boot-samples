package com.example.filter;

import com.example.vo.LoginUserInfo;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:06
 * @Classname AccessContext
 * @Description 使用 ThreadLocal 存储每个线程携带的 userLoginInfo 信息
 * 注意：要及时清除存储在 ThreadLocal 中的用户信息，防止内存泄露
 * 1. 保证没有资源泄露
 * 2. 保证线程再从用时，数据不混乱
 */
public class AccessContext {
    private static final ThreadLocal<LoginUserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static LoginUserInfo getLoginUserInfo() {
        return USER_INFO_THREAD_LOCAL.get();
    }

    public static void setUserInfoThreadLocal(LoginUserInfo loginUserInfo){
        USER_INFO_THREAD_LOCAL.set(loginUserInfo);
    }

    public static void clearLoginUserInfo(){
        USER_INFO_THREAD_LOCAL.remove();
    }


}
