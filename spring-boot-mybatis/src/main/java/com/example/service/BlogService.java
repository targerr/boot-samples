package com.example.service;

import com.example.entity.Blog;

/**
 * @Author: wgs
 * @Date 2022/4/21 13:53
 * @Classname BlogService
 * @Description
 */
public interface BlogService {
    int add(Blog blog);
    int update(Blog blog);
    int deleteBysno(String id);
    Blog queryStudentBySno(String id);
}
