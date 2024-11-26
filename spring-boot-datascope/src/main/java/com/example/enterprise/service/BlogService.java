package com.example.enterprise.service;

import com.example.enterprise.entity.Blog;
import com.example.enterprise.api.model.vo.BlogVO;
import com.example.enterprise.api.model.dto.BlogDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
public interface BlogService {


    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-11-21
    * @param page: 分页入参
    * @param blogDto: DTO分页查询对象
    * @return Page<BlogVO>
    */
    Page<BlogVO> insidePageEnhance(Page<Blog> page, BlogDTO blogDto);


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-11-21
     * @param blogDto: DTO查询对象
     * @return List<BlogVO>
     */
    List<BlogVO> insideListEnhance(BlogDTO blogDto);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-11-21
     * @param id: id
     * @return BlogVO
     */
    BlogVO insideGetEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-11-21
     * @param blogDto: DTO查询对象
     * @return BlogVO
     */
    BlogVO insideGetOneEnhanceByParam(BlogDTO blogDto);

    /**
     * 新增
     * @author wgs
     * @since 2024-11-21
     * @param blogDto: 新增DTO
     * @return Long
     */
     Long insideSaveEnhance(BlogDTO blogDto);


    /**
     * 修改
     * @author wgs
     * @since 2024-11-21
     * @param blogDto: 修改DTO
     * @return Long
     */
     Long insideUpdateEnhance(BlogDTO blogDto);


    /**
     * 删除
     * @author wgs
     * @since 2024-11-21
     * @param id: id
     * @return Boolean
     */
    Boolean insideRemoveEnhanceById(Long id);
}
