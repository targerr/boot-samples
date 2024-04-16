package com.example.springbootyml.config;

/**
 * @Author: wgs
 * @Date 2024/4/16 10:01
 * @Classname TaikangProperties
 * @Description
 */
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@ConfigurationProperties(prefix = "taikang")
@Data
public class TaikangProperties {

    /**
     * 渠道码
     */
    private String channelcode;
    /**
     * 密钥
     */
    private String signSecret;
    /**
     * 投保接口
     */
    private String insureUrl;
    /**
     * 百万医疗
     */
    private MedicalConfig medical;
    /**
     * 旅游险
     */
    private TravelConfig travel;


    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MedicalConfig {
        private BaseProperties monthConfig;
        private BaseProperties yearConfig;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TravelConfig {
        private BaseProperties monthConfig;
        private BaseProperties yearConfig;
    }



    @Data
    public static class BaseProperties {
        /**
         * 分享链接参数
         */
        private String tkOpenFrom;
        /**
         * 产品代码
         * 即销售方案代码
         */
        private String productCode;

        /**
         * 选填
         * 年缴\月缴
         * 定额方案代码
         * 部分产品必传
         */
        private String comboCode;

        /**
         * 描述
         */
        private String msg;

        /**
         * 年缴\月缴
         * 魔方组合编号
         */
        private String discountSubType;
        /**
         * 基础版
         * 总保额 分
         */
        private String baseTotalAmount;
        /**
         * 升级版
         * 总保额 分
         */
        private String upgradeTotalAmount;

        /**
         * 基础险别信息
         * "[{\"amount\":3000000,\"kindCode\":\"PYL00310Z326\"},{\"amount\":6000000,\"kindCode\":\"PYL00310Z328\"}]";
         */
        private String baseRiskList;

        /**
         * 升级险别信息
         */
        private String upgradeRiskList;

    }


}
