package com.example.service;

import com.example.dto.BaseRspDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;

/**
 * @Author: wgs
 * @Date 2022/5/24 13:52
 * @Classname TaskHandler
 * @Description
 */
public interface TaskHandler {

    /**
     * 1.指定子类
     * 2.策略类的key，如是usetInfoDTO还是bannerDTO，还是labelDTO
     * @return
     */
    ServiceStrategy getTaskType();

    /**
     * 处理器
     * @param req
     * @return
     */
    BaseRspDTO<Object> execute(AppInfoReq req);

}
