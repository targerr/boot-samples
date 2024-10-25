package com.github.common.log.service;

import com.github.common.log.pojo.LogOperateDo;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:39
 * @Classname RemoteLogOperateService
 * @Description
 */
public interface RemoteLogOperateService {
    /**
     * 保存日志
     *
     * @param logOperate 日志实体
     * @return succes、false
     */
    boolean save(LogOperateDo logOperate);
}
