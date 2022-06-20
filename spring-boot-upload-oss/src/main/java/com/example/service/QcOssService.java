package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.model.UploadFileRequest;
import com.example.config.QcOssYmlConfig;
import com.example.constant.OssSavePlaceEnum;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2022/6/20 10:32
 * @Classname QcOssService
 * @Description https://cloud.tencent.com/document/product/436/10199
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "qc-oss")
public class QcOssService implements IOssService {
    @Autowired
    private QcOssYmlConfig qcOssYmlConfig;

    private COSClient client;

    @PostConstruct
    private void init() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(qcOssYmlConfig.getSecretId(), qcOssYmlConfig.getSecretKey());
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("COS_REGION");
        //初始化客户端配置
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        client = new COSClient(cred, clientConfig);
    }

    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        //腾讯云必需要以"/"开头

        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "exampleobject";
        PutObjectRequest putObjectRequest = null;
        try {
            putObjectRequest = new PutObjectRequest(qcOssYmlConfig.getPublicBucketName(), key, multipartFile.getInputStream(), null);
            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                return "https://" + qcOssYmlConfig.getPublicBucketName() + "." + qcOssYmlConfig.getEndpoint() + "/" + saveDirAndFileName;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}
