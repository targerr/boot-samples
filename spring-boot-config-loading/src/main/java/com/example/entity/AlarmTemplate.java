package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2025/1/14 17:03
 * @Classname AlarmTemplate
 * @Description 告警模板实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmTemplate implements Serializable {

    private static final long serialVersionUID = -3287747093418563016L;
    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;
}
