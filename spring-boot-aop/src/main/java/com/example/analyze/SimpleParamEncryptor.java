package com.example.analyze;

import java.util.Base64;

/**
 * @Author: wgs
 * @Date 2024/10/16 15:34
 * @Classname SimpleParamEncryptor
 * @Description
 */
public class SimpleParamEncryptor implements ParamAnalyze<String>{
    @Override
    public <E> String analyzeParam(E data) {
        // 将数据转换为字符串，然后进行Base64编码
        String rawData = data.toString();
        return Base64.getEncoder().encodeToString(rawData.getBytes());
    }

    @Override
    public String templateId() {
        // 返回一个固定的字符串作为算法标识ID
        return "SIMPLE_ENCRYPTION_TEMPLATE";
    }
}
