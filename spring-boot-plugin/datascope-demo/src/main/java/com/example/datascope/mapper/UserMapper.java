package com.example.datascope.mapper;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.datascope.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 查询用户列表（带数据权限）
	 *
	 * @param dataScope 数据权限对象
	 * @return 用户列表
	 */
	List<User> selectListWithDataScope(@Param("dataScope") DataScope dataScope);

	/**
	 * 根据用户名查询用户
	 *
	 * @param username 用户名
	 * @return 用户信息
	 */
	User selectByUsername(@Param("username") String username);

}
