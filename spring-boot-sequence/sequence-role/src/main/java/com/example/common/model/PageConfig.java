package com.example.common.model;

/**
 * @Author: wgs
 * @Date 2024/9/25 15:31
 * @Classname PageConfig
 * @Description
 */
public class PageConfig {
    public static final Integer MIN_SIZE = 10;

    public static final Integer MAX_SIZE = 100;

    public static Integer currentPagination(Integer current) {
        if (null == current || current < 1) {
            return 1;
        }
        return current;
    }

    public static Integer pageSize(Integer pageSize) {
        if (null == pageSize || pageSize < MIN_SIZE) {
            return MIN_SIZE;
        } else if (pageSize > MAX_SIZE) {
            return MAX_SIZE;
        }
        return pageSize;
    }
}
