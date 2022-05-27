package com.example;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @Author: wgs
 * @Date 2022/5/27 14:19
 * @Classname OssClientFactoryBean
 * @Description
 */
public class OssClientFactoryBean implements FactoryBean<OSS>, InitializingBean, DisposableBean {
    private OSS oss;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

    @Override
    public void destroy() throws Exception {
        if (this.oss != null) {
            this.oss.shutdown();
        }
    }

    @Override
    public OSS getObject() throws Exception {
        return this.oss;
    }

    @Override
    public Class<?> getObjectType() {
        return OSSClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.endpoint, "'endpoint' must be not null");
        Assert.notNull(this.accessKeyId, "'accessKeyId' must be not null");
        Assert.notNull(this.accessKeySecret, "'accessKeySecret' must be not null");
        this.oss = new OSSClientBuilder().build(endpoint,accessKeyId, accessKeySecret);
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(final String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(final String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
