package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/11/17 15:08
 * @Classname AsyncTaskStatusEnum
 * @Description 异步任务状态类
 */
@AllArgsConstructor
@Getter
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
