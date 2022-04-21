package com.example.controller;

import com.example.entity.Blog;
import com.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/4/21 14:05
 * @Classname BlogController
 * @Description
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @RequestMapping( value = "/selectOne", method = RequestMethod.GET)
    public Blog queryStudentBySno(String id) {
        return blogService.queryStudentBySno(id);
    }

}
