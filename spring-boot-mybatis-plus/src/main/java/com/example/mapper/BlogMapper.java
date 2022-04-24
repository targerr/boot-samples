package com.example.mapper;

import com.example.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanggs
 * @since 2022-04-22
 */
public interface BlogMapper extends BaseMapper<Blog> {

    void deleteByPrimaryKey(String id);
}
