package com.example.service;

import com.example.dto.BaseUserInfoDTO;
import com.example.dto.UserInfoDTO;
import com.example.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.UserReq;

/**
* @author wanggaoshuai
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2023-10-17 14:47:42
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 暗语
     * @return {@link UserInfoDTO}
     */
    UserInfoDTO login(String username, String password);

    /**
     * 登记
     *
     * @param userReq 用户需求
     * @return {@link String}
     */
    String register(UserReq userReq);

    /**
     * 获取登录的用户信息,并更行丢对应的ip信息
     *
     * @param session  用户会话
     * @param clientIp 用户最新的登录ip
     * @return 返回用户基本信息
     */
    BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp);
}
