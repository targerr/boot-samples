package com.itlaoqi.springai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * 物流信息结构化抽取的目标实体（JavaBean）。
 * 使用 @Data 生成标准的 getter/setter 等方法。
 * 通过 @JsonProperty 和 @JsonPropertyDescription 为模型提供字段语义与格式引导。
 */
@Data
public class LogisticsInfo {

    @JsonProperty("orderId")
    //引入 Spring AI 的 BeanOutputConverter （为 POJO 自动生成 JSON Schema 并嵌入到提示中），这些描述会进入 Schema 的 description ，提升结构化抽取的准确性。
    @JsonPropertyDescription("订单号：电商订单的唯一标识，例如 202409-XYZ-8899")
    private String orderId;

    @JsonProperty("currentLocation")
    @JsonPropertyDescription("当前位置：快件当前所在城市或网点，例如 杭州滨江转运中心")
    private String currentLocation;

    @JsonProperty("estimatedArrival")
    @JsonPropertyDescription("预计到达时间：ISO日期时间或自然语言，例如 2025-10-22 18:00 或 明晚")
    private String estimatedArrival;

    @JsonProperty("courierName")
    @JsonPropertyDescription("快递员姓名：负责最后一公里派送的人员姓名")
    private String courierName;

    @JsonProperty("courierPhone")
    @JsonPropertyDescription("快递员联系方式：手机号码或座机，优先手机号")
    private String courierPhone;
}