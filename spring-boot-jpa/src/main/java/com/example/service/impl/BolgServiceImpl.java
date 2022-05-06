package com.example.service.impl;

import com.example.entity.Blog;
import com.example.repository.BlogRepository;
import com.example.service.BolgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/4/24 11:17
 * @Classname BolgServiceImpl
 * @Description
 */
@Service
@Slf4j
public class BolgServiceImpl implements BolgService {
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Blog findOneBlog(String id) {
        return blogRepository.getOne(id);
    }
}
