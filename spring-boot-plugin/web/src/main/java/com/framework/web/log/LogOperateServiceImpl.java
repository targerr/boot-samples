package com.framework.web.log;

import com.alibaba.fastjson.JSONObject;
import com.github.common.log.pojo.LogOperateDo;
import com.github.common.log.service.RemoteLogOperateService;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:45
 * @Classname LogoperateServiceImpl
 * @Description
 */
@Service
public class LogOperateServiceImpl implements RemoteLogOperateService {
    @Override
    public boolean save(LogOperateDo logOperate) {
        System.err.println("操作日志: " + JSONObject.toJSONString(logOperate, true));
        return false;
    }
}
