package org.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Blog;
import org.example.param.BlogParam;
import org.example.result.Result;
import org.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2025-08-17
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/page")
    public Result getBlogMasterPage(Page<Blog> pageParam, BlogParam blogParam) {
        Page<Blog> blogPage = blogService.getBlogPage(pageParam, blogParam);
        return Result.success(blogPage);
    }

    @GetMapping("/slavePage")
    public Result getSlaveBlogPage(Page<Blog> pageParam, BlogParam blogParam) {
        Page<Blog> blogPage = blogService.getSlaveBlogPage(pageParam, blogParam);
        return Result.success(blogPage);
    }

    @PostMapping("/save")
    public Result saveBlog(@RequestBody Blog Blog) {
        boolean flag = blogService.saveOrUpdate(Blog);
        if (flag) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("/info/{id}")
    public Result getInfo(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        return Result.success(blog);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteBlog(@PathVariable Long id) {
        boolean flag = blogService.removeById(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
