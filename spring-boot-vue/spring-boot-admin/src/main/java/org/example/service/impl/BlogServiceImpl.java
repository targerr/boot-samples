package org.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Blog;
import org.example.mapper.BlogMapper;
import org.example.param.BlogParam;
import org.example.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-10-23
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @DS("master")// 主库，如果没有加注解，默认还是主库
    @Override
    public Page<Blog> getBlogPage(Page<Blog> pageParam, BlogParam blogParam) {
        Page<Blog> page = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(blogParam.getType()), Blog::getType, blogParam.getType())
                .eq(null != blogParam.getUserId(), Blog::getUserId, blogParam.getUserId())
                .like(StrUtil.isNotBlank(blogParam.getTitle()), Blog::getTitle, blogParam.getTitle())
                .orderByDesc(Blog::getCreateTime)
                .page(pageParam);
        return  page;
    }
    @DS("slave") // 从库
    @Override
    public Page<Blog> getSlaveBlogPage(Page<Blog> pageParam, BlogParam blogParam) {
        Page<Blog> page = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(blogParam.getType()), Blog::getType, blogParam.getType())
                .eq(null != blogParam.getUserId(), Blog::getUserId, blogParam.getUserId())
                .like(StrUtil.isNotBlank(blogParam.getTitle()), Blog::getTitle, blogParam.getTitle())
                .orderByDesc(Blog::getCreateTime)
                .page(pageParam);
        return  page;
    }
}
