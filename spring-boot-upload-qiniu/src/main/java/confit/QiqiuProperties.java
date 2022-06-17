package confit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/17 09:29
 * @Classname QiqiuProperties
 * @Description
 */
@Configuration
@ConfigurationProperties(prefix = "qiniu.config")
@Data
public class QiqiuProperties {
    private String ak;
    private String sk;
    private String bucket;
}
