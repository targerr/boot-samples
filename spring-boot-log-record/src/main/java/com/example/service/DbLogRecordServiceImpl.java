package com.example.service;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.IOperationLogGetService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/16 17:11
 * @Classname UserParseFunction
 * @Description
 */
@Service
@Slf4j
public class DbLogRecordServiceImpl implements IOperationLogGetService {


    @Override
    public void createLog(LogDTO logDTO) {
        log.info("logDTO: [{}]", JSON.toJSONString(logDTO));
    }
}