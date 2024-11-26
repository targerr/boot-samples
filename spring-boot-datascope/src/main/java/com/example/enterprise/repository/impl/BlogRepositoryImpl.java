package com.example.enterprise.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.enterprise.dao.mapper.BlogMapper;
import com.example.enterprise.entity.Blog;
import com.example.enterprise.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 仓储实现类
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
@Slf4j
@Service
public class BlogRepositoryImpl extends ServiceImpl<BlogMapper, Blog> implements BlogRepository {


    /**
     * 集合条件查询
     *
     * @param blog:
     * @return List<Blog>
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    public List<Blog> listEnhance(Blog blog) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>(blog);
        buildListQueryWrapper(blog, queryWrapper);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 构造list查询条件
     *
     * @param blog:
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-11-21
     */
    private void buildListQueryWrapper(Blog blog, QueryWrapper<Blog> queryWrapper) {

    }

    /**
     * 集合,增强返回参数追加
     *
     * @param blogList:
     * @return List<Blog>
     * @author wgs
     * @since 2024-11-21
     */
    private List<Blog> assignment(List<Blog> blogList) {
        blogList.forEach(blog -> {
        });
        return blogList;
    }

    /**
     * 分页条件查询
     *
     * @param blog:
     * @param page: 分页入参
     * @return Page<Blog>
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    public Page<Blog> pageEnhance(Page<Blog> page, Blog blog) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>(blog);
        buildPageQueryWrapper(blog, queryWrapper);
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 构造分页查询条件
     *
     * @param blog:
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-11-21
     */
    private void buildPageQueryWrapper(Blog blog, QueryWrapper<Blog> queryWrapper) {

    }

    /**
     * 分页,增强返回参数追加
     *
     * @param blogList:
     * @return Page<Blog>
     * @author wgs
     * @since 2024-11-21
     */
    private Page<Blog> assignment(Page<Blog> blogList) {
        blogList.getRecords().forEach(blog -> {
        });
        return blogList;
    }

    /**
     * 单条条件查询
     *
     * @param id: id
     * @return Blog
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    public Blog getOneEnhanceById(Long id) {
        return assignment(baseMapper.selectById(id));
    }

    /**
     * 根据查询参数查询单条结果
     *
     * @param blog:
     * @return Blog
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    public Blog getOneByParam(Blog blog) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>(blog);
        buildOneQueryWrapper(blog, queryWrapper);
        return assignment(baseMapper.selectOne(queryWrapper));
    }

    @Override
    public Long saveEnhance(Blog blog) {
        return null;
    }

    /**
     * 构造单条查询条件
     *
     * @param blog:
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-11-21
     */
    private void buildOneQueryWrapper(Blog blog, QueryWrapper<Blog> queryWrapper) {

    }

    /**
     * 单条，增强返回参数追加
     *
     * @param blog:
     * @return Blog
     * @author wgs
     * @since 2024-11-21
     */
    private Blog assignment(Blog blog) {
        return blog;
    }



    /**
     * 修改
     *
     * @param blog:
     * @return Boolean
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long updateEnhance(Blog blog) {
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(blog, updateWrapper);
        boolean result = baseMapper.update(blog, updateWrapper) > 0;
        return null;
    }

    /**
     * 构造更新条件
     *
     * @param blog:
     * @param updateWrapper: 更新Wrapper
     * @author wgs
     * @since 2024-11-21
     */
    private void buildUpdateWrapper(Blog blog, LambdaUpdateWrapper<Blog> updateWrapper) {
        // 更新条件以及设值
        updateWrapper.eq(Blog::getId, blog.getId());
    }

    /**
     * 删除
     *
     * @param id: id
     * @return Boolean
     * @author wgs
     * @since 2024-11-21
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean removeEnhanceById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}
