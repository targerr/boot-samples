package com.example.command;

import com.example.dto.BaseRspDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import com.example.service.HandlerHolder;

import java.util.concurrent.Callable;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:18
 * @Classname BaseTaskCommand
 * @Description
 */
public class BaseTaskCommand implements Callable<BaseRspDTO<Object>> {
    private ServiceStrategy key;
    private AppInfoReq req;
    private HandlerHolder handlerHolder;


    public BaseTaskCommand(ServiceStrategy key, AppInfoReq req, HandlerHolder handlerHolder) {
        this.key = key;
        this.req = req;
        this.handlerHolder = handlerHolder;
    }

    @Override
    public BaseRspDTO<Object> call() throws Exception {
        return handlerHolder.route(key).execute(req);
    }
}
