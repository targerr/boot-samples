package com.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.web.entity.Blog;

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
