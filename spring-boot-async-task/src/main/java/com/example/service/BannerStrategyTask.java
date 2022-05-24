package com.example.service;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.BannerDTO;
import com.example.dto.BaseRspDTO;
import com.example.dto.LabelDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:03
 * @Classname UserInfoStrategyTask
 * @Description 用户信息策略类
 */
@Service
@Slf4j
public class BannerStrategyTask implements TaskHandler{

    @Override
    public ServiceStrategy getTaskType() {
        return ServiceStrategy.BANNER;
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        log.debug("banner 查询执行 线程Id:{}",Thread.currentThread().getId());
        BaseRspDTO<Object> baseRspDTO = new BaseRspDTO<Object>();
        baseRspDTO.setKey(getTaskType());
        baseRspDTO.setData(BannerDTO.builder().msg("查询banner图!").build());
        ThreadUtil.sleep(3000);
        return baseRspDTO;
    }

}
