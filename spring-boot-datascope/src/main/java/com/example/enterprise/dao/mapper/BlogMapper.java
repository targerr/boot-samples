package com.example.enterprise.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.enterprise.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
//public interface BlogMapper extends DataPermissionMapper<Blog> {

    List<Blog> getList(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    // @DataPermissionAnno
    List<Blog> selectAllCustomer();
}
