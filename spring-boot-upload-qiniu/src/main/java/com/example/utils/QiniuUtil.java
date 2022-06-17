package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import confit.QiqiuProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author: wgs
 * @Date 2022/6/17 09:33
 * @Classname QiniuUtil
 * @Description https://developer.qiniu.com/kodo/1239/java#upload-flow
 */
@Component
@Slf4j
public class QiniuUtil {
    @Resource
    private QiqiuProperties qiqiuProperties;

    /**
     * 上传
     *
     * @param file 文件地址
     * @param name 上传地址
     * @throws Exception
     */
    public void upload(String localFilePath, String name) throws Exception {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.regionNa0());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(qiqiuProperties.getBucket(), qiqiuProperties.getSk());

        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");

        String upToken = auth.uploadToken(qiqiuProperties.getBucket(), null, 3600, putPolicy);

        Response response = uploadManager.put(localFilePath, name, upToken);
        //解析上传成功的结果
        log.info("【七牛云】 上传响应,data{}",JSONObject.parseObject(response.bodyString()));
        DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);

    }

}
