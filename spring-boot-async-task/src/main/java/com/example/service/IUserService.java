package com.example.service;

import com.example.dto.UserInfoDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import com.example.param.UserInfoParam;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/24 11:08
 * @Classname IUserService
 * @Description
 */
@Service
public class IUserService {
    UserInfoDTO queryUserInfo(UserInfoParam userInfoParam) {
      return   UserInfoDTO.builder().msg("查询用户信息").build();
    }

    public UserInfoParam buildUserParam(AppInfoReq req) {
        return UserInfoParam.builder().build();
    }
}
