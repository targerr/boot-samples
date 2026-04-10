package com.example.datascope.service;

import com.albedo.java.plugins.datascope.annotation.DataScopeAnno;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.datascope.entity.User;
import com.example.datascope.mapper.UserMapper;
import com.example.datascope.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务
 */
@Slf4j
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@DataScopeAnno(scopeName = "id", creatorName = "created_by")
	public List<User> queryUsers() {
		// AOP 切面会自动处理数据权限
		LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
		return userMapper.selectList(query);
	}

	/**
	 * 查询所有用户（带数据权限过滤）
	 * 会根据当前登录用户的数据权限自动过滤
	 */
	public List<User> queryAllUsers() {
		// 从当前登录用户获取数据权限
		DataScope dataScope = getCurrentUserDataScope();

		log.info("当前用户数据权限: isAll={}, deptIds={}, isSelf={}",
			dataScope.isAll(), dataScope.getDeptIds(), dataScope.isSelf());

		// 传递给 Mapper，拦截器会自动修改 SQL
		List<User> users = userMapper.selectListWithDataScope(dataScope);

		log.info("查询到 {} 条用户记录", users.size());

		return users;
	}

	/**
	 * 查询用户（不过滤数据权限）
	 * 仅管理员可用
	 */
	public List<User> queryAllUsersWithoutFilter() {
		// 创建全部数据权限对象，不进行过滤
		DataScope dataScope = DataScopeUtil.createAll();
		return userMapper.selectListWithDataScope(dataScope);
	}

	/**
	 * 根据用户名查询用户
	 */
	public User getUserByUsername(String username) {
		return userMapper.selectByUsername(username);
	}

	/**
	 * 获取当前用户的数据权限
	 */
	private DataScope getCurrentUserDataScope() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof CustomUserDetails) {
				return ((CustomUserDetails) principal).getDataScope();
			}
		} catch (Exception e) {
			log.warn("获取当前用户数据权限失败: {}", e.getMessage());
		}

		// 如果获取失败，返回空权限（不过滤）
		return DataScopeUtil.createEmpty();
	}

}
