package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/11/16 17:12
 * @Classname AsyncTaskStatusEnum
 * @Description 线程状态枚举
 */
@Getter
@AllArgsConstructor
public enum AsyncTaskStatusEnum {


    STARTED(0, "已经启动"),
    RUNNING(1, "正在运行"),
    SUCCESS(2, "执行成功"),
    FAILED(3, "执行失败"),
    ;

    /** 执行状态编码 */
    private final int state;

    /** 执行状态描述 */
    private final String stateInfo;
}
