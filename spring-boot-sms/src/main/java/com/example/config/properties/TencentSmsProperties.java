package com.example.config.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯短信参数
 *
 * 参数示例：
 * [{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"AKIDhDUUDfffffMEqBF1WljQq","secretKey":"B4h39yWnfffff7D2btue7JErDJ8gxyi","smsSdkAppId":"140025","templateId":"11897","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}}]
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Configuration
//@ConfigurationProperties(prefix = "ali.config")
public class TencentSmsProperties {

    /**
     * api相关
     */
    private String url;
    private String region ;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

    /**
     * 标识渠道商Id
     */
    private Integer supplierId;

    /**
     * 标识渠道商名字
     */
    private String supplierName;

}
