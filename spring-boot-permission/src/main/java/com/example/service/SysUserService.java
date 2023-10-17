package com.example.service;

import com.example.dto.UserInfoDTO;
import com.example.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
