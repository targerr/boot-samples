package com.example.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.example.config.AliyunOssYmlConfig;
import com.example.constant.OssSavePlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @Author: wgs
 * @Date 2022/6/18 09:46
 * @Classname AliyunOssService
 * @Description
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "aliyun-oss")
public class AliyunOssService implements IOssService {
    @Autowired
    private AliyunOssYmlConfig aliyunOssYmlConfig;

    // ossClient初始化
    OSS ossClient = null;

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(aliyunOssYmlConfig.getEndpoint(), aliyunOssYmlConfig.getAccessKeyId(), aliyunOssYmlConfig.getAccessKeySecret());

    }

    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        try {
            this.ossClient.putObject(ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ? aliyunOssYmlConfig.getPublicBucketName() : aliyunOssYmlConfig.getPrivateBucketName()
                    , saveDirAndFileName, multipartFile.getInputStream());

            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                // 文档：https://www.alibabacloud.com/help/zh/doc-detail/39607.htm  example: https://BucketName.Endpoint/ObjectName
                return "https://" + aliyunOssYmlConfig.getPublicBucketName() + "." + aliyunOssYmlConfig.getEndpoint() + "/" + saveDirAndFileName;
            }

            return saveDirAndFileName;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        try {
            String bucket = ossSavePlaceEnum == OssSavePlaceEnum.PRIVATE ? aliyunOssYmlConfig.getPrivateBucketName() : aliyunOssYmlConfig.getPublicBucketName();
            this.ossClient.getObject(new GetObjectRequest(bucket, source), new File(target));

            return true;
        } catch (Exception e) {
            log.error("error", e);
            return false;
        }
    }
}
