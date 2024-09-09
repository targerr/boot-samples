package com.example.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.example.entity.Blog;
import com.example.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggs
 * @since 2022-04-22
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService iBlogService;

    @GetMapping("/getDetail")
    public Blog getDetail(String id) {
        String peek = DynamicDataSourceContextHolder.peek();
        System.out.println("获得当前线程数据源: " + peek);


        return iBlogService.getById(id);
    }

}
