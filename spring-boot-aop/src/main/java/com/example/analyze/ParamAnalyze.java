package com.example.analyze;

/**
 * @Author: wgs
 * @Date 2024/10/16 13:59
 * @Classname ParmAnalyze
 * @Description 参数解析接口
 */
public interface ParamAnalyze <T> {

    /**
     * 加密
     *
     * @param data 数据
     * @return T
     */
    <E> String analyzeParam(E data);

    /**
     * 唯一算法标识id
     *
     * @return String
     */
    String templateId();
}
