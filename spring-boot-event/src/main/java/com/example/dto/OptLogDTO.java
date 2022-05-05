package com.example.dto;

import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/5/5 10:05
 * @Classname OptLogDTO
 * @Description
 */
@Data
public class OptLogDTO {
    /**
     * 线程id
     */
    private String threadId;
    /**
     * 线程名称
     */
    private String threadName;
    /**
     * 线程名称
     */
    private String ip;
    /**
     * url
     */
    private String url;
    /**
     * 描述
     */
    private String description;
}
