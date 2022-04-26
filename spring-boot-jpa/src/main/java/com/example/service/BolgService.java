package com.example.service;

import com.example.entity.Blog;

/**
 * @Author: wgs
 * @Date 2022/4/24 11:11
 * @Classname BolgService
 * @Description
 */
public interface BolgService {
    /**
     * 查询单个
     * @param id
     * @return
     */
    Blog findOneBlog(String id);
}
