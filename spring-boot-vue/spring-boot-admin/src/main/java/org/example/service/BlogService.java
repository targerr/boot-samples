package org.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.param.BlogParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-10-23
 */
public interface BlogService extends IService<Blog> {

    Page<Blog>  getBlogPage(Page<Blog> pageParam, BlogParam blogParam);

    Page<Blog> getSlaveBlogPage(Page<Blog> pageParam, BlogParam blogParam);
}
