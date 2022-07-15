package com.example.chain.pipeline;

/**
 * @Author: wgs
 * @Date 2022/7/14 16:53
 * @Classname BusinessProcess
 * @Description 业务执行器
 */
public interface BusinessProcess <T extends ProcessModel>{
    /**
     * 真正处理逻辑
     * @param context
     */
    public abstract void process(ProcessContext<T> context);
}
