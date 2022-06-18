package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.config.QiniuOssYmlConfig;
import com.example.constant.OssSavePlaceEnum;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2022/6/18 10:42
 * @Classname QiniuOssServie
 * @Description
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "qiniu-oss")
public class QiniuOssService implements IOssService {

    @Autowired
    private QiniuOssYmlConfig qiqiuProperties;

    private UploadManager uploadManager;
    private String token;

    @PostConstruct
    public void init() {
        //构造一个带指定 Region 对象的配置类
        uploadManager = new UploadManager(new Configuration(Region.regionNa0()));
        token = Auth.create(qiqiuProperties.getAk(), qiqiuProperties.getSk()).
                uploadToken(qiqiuProperties.getBucket());
    }

    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        Response response = null;
        try {
            response = uploadManager.put(multipartFile.getBytes(), saveDirAndFileName, token);
            //解析上传成功的结果
            log.info("【七牛云】 上传响应,data{}", JSONObject.parseObject(response.bodyString()));
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);


            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                // 文档：https://developer.qiniu.com/kodo/1239/java#upload-flow
                // 七牛绑定的域名
                return "绑定域名" + "/" + saveDirAndFileName;
            }

            return saveDirAndFileName;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }


    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}
