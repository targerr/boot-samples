package com.example.service.impl;

import com.example.dao.BlogMapper;
import com.example.entity.Blog;
import com.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/4/21 13:56
 * @Classname BlogServiceImpl
 * @Description
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;

    @Override
    public int add(Blog blog) {
        return blogMapper.insert(blog);
    }

    @Override
    public int update(Blog blog) {
        return blogMapper.updateByPrimaryKey(blog);
    }

    @Override
    public int deleteBysno(String id) {
        return blogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Blog queryStudentBySno(String id) {
        return blogMapper.selectByPrimaryKey(id);
    }
}
