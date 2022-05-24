package com.example.service;

import cn.hutool.core.thread.ThreadUtil;
import com.example.dto.BannerDTO;
import com.example.dto.BaseRspDTO;
import com.example.dto.LabelDTO;
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
public class LabelStrategyTask implements TaskHandler {

    @Override
    public ServiceStrategy getTaskType() {
        return ServiceStrategy.LABEL;
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        log.debug("执行标签查询!线程Id:{}", Thread.currentThread().getId());
        BaseRspDTO<Object> baseRspDTO = new BaseRspDTO<Object>();
        baseRspDTO.setKey(getTaskType());
        baseRspDTO.setData(LabelDTO.builder().msg("查询banner图!").build());
        ThreadUtil.sleep(2000);
        return baseRspDTO;
    }
}
