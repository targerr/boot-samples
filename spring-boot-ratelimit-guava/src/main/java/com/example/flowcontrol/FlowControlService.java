package com.example.flowcontrol;

/**
 * @Author: wgs
 * @Date 2022/5/23 14:30
 * @Classname FlowControlService
 * @Description 流量控制服务
 */
public interface FlowControlService {
    /**
     * 流量控制
     * @param flowControlParam
     */
    public abstract void flowControl(FlowControlParam flowControlParam);
}
