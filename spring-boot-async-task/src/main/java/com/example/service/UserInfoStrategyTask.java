package com.example.service;

import com.example.dto.BaseRspDTO;
import com.example.dto.UserInfoDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import com.example.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:03
 * @Classname UserInfoStrategyTask
 * @Description 用户信息策略类
 */
@Service
@Slf4j
public class UserInfoStrategyTask implements TaskHandler {
    @Autowired
    private IUserService userService;

    @Override
    public ServiceStrategy getTaskType() {
        return ServiceStrategy.USER;
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        log.debug("用户查询执行!线程Id:{}", Thread.currentThread().getId());
        UserInfoParam userInfoParam = userService.buildUserParam(req);
        UserInfoDTO userInfoDTO = userService.queryUserInfo(userInfoParam);
        BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
        userBaseRspDTO.setKey(getTaskType());
        userBaseRspDTO.setData(userInfoDTO);
        return userBaseRspDTO;
    }
}
