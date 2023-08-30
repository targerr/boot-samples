package com.example.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: wgs
 * @Date 2023/8/25 10:45
 * @Classname AppProperties
 * @Description
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "wgs")
public class AppProperties {
    private String name;

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    private SmsProvider smsProvider = new SmsProvider();

    @Getter
    @Setter
    private EmailProvider emailProvider = new EmailProvider();

    @Getter
    @Setter
    private LeanCloud leanCloud = new LeanCloud();

    @Getter
    @Setter
    private Ali ali = new Ali();

    @Getter
    @Setter
    public static class Jwt {

        private String header = "Authorization"; // HTTP 报头的认证字段的 key

        private String prefix = "Bearer "; // HTTP 报头的认证字段的值的前缀

        private long accessTokenExpireTime = 60 * 1000L; // Access Token 过期时间

        private long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L; // Refresh Token 过期时间

        private String key;

        private String refreshKey;
    }

    @Getter
    @Setter
    public static class LeanCloud {
        private String appId;
        private String appKey;
    }

    @Getter
    @Setter
    public static class Ali {
        private String apiKey;
        private String apiSecret;
    }

    @Getter
    @Setter
    public static class SmsProvider {
        private String name;
        private String apiUrl;
    }

    @Getter
    @Setter
    public static class EmailProvider {
        private String name;
        private String apiKey;
    }
}
