package com.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * description: 模板渠道
 *
 * @Author: wgs
 * @Date 2025/1/14 17:17
 */
@Getter
@RequiredArgsConstructor
public enum TemplateChannel {
    JDBC,
    FILE,
    YAML

}
