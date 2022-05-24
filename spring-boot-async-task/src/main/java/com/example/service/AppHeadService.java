package com.example.service;

import cn.hutool.core.thread.ExecutorBuilder;
import com.alibaba.fastjson.JSONObject;
import com.example.command.BaseTaskCommand;
import com.example.dto.BannerDTO;
import com.example.dto.BaseRspDTO;
import com.example.dto.LabelDTO;
import com.example.dto.UserInfoDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import com.example.resp.AppHeadInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:27
 * @Classname AppHeadService
 * @Description 获取首页信息
 */
@Service
public class AppHeadService {
    @Autowired
    ParallelInvokeCommonService parallelInvokeCommonService;
    @Autowired
    HandlerHolder handlerHolder;

    public AppHeadInfoResponse parallelQueryAppHeadPageInfo2(AppInfoReq req) {

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();

        long beginTime = System.currentTimeMillis();
        List<Callable<BaseRspDTO<Object>>> taskList = new ArrayList<>();
        //用户信息查询任务
        taskList.add(new BaseTaskCommand(ServiceStrategy.USER, req, handlerHolder));
        //banner查询任务
        taskList.add(new BaseTaskCommand(ServiceStrategy.BANNER, req, handlerHolder));
        //标签查询任务
        taskList.add(new BaseTaskCommand(ServiceStrategy.LABEL, req, handlerHolder));

        List<BaseRspDTO<Object>> resultList = parallelInvokeCommonService.executeTask(taskList, executor);

        if (resultList == null || resultList.size() == 0) {
            return new AppHeadInfoResponse();
        }

        UserInfoDTO userInfoDTO = null;
        BannerDTO bannerDTO = null;
        LabelDTO labelDTO = null;

        for (BaseRspDTO<Object> baseRspDTO : resultList) {
            if (ServiceStrategy.USER.equals(baseRspDTO.getKey())) {
                userInfoDTO = (UserInfoDTO) baseRspDTO.getData();
            } else if (ServiceStrategy.BANNER.equals(baseRspDTO.getKey())) {
                bannerDTO = (BannerDTO) baseRspDTO.getData();
            } else if (ServiceStrategy.LABEL.equals(baseRspDTO.getKey())) {
                labelDTO = (LabelDTO) baseRspDTO.getData();
            }
        }

        System.out.println("结束并行查询app首页信息（最终版本）,总耗时：" + (System.currentTimeMillis() - beginTime));
        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }

    private AppHeadInfoResponse buildResponse(UserInfoDTO userInfoDTO, BannerDTO bannerDTO, LabelDTO labelDTO) {
        return AppHeadInfoResponse
                .builder()
                .userInfoDTO(userInfoDTO)
                .bannerDTO(bannerDTO)
                .labelDTO(labelDTO)
                .build();
    }
}
