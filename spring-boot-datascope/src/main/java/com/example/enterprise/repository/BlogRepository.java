package com.example.enterprise.repository;

import com.example.enterprise.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 *  仓储类
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
public interface BlogRepository extends IService<Blog> {


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-11-21
     * @param blog: 
     * @return List<Blog>
     */
    List<Blog> listEnhance(Blog blog);


    /**
     * 分页条件查询
     * @author wgs
     * @since 2024-11-21
     * @param page: 分页入参
     * @param blog: 
     * @return Page<Blog>
     */
    Page<Blog> pageEnhance(Page<Blog> page, Blog blog);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-11-21
     * @param id: id
     * @return Blog
     */
    Blog getOneEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-11-21
     * @param blog: 
     * @return Blog
     */
    Blog getOneByParam(Blog blog);

    /**
     * 新增
     * @author wgs
     * @since 2024-11-21
     * @param blog: 
     * @return Long
     */
    Long saveEnhance(Blog blog);


    /**
     * 修改
     * @author wgs
     * @since 2024-11-21
     * @param blog: 
     * @return Long
     */
    Long updateEnhance(Blog blog);


    /**
     * 删除
     * @author wgs
     * @since 2024-11-21
     * @param id: id
     * @return Boolean
     */
    Boolean removeEnhanceById(Long id);
}
