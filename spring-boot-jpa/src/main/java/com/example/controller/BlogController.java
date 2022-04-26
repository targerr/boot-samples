package com.example.controller;

import com.example.entity.Blog;
import com.example.service.BolgService;
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
    private BolgService bolgService;

    @GetMapping("/getDetail")
    public Blog getDetail(String id) {
        return bolgService.findOneBlog(id);
    }

}
