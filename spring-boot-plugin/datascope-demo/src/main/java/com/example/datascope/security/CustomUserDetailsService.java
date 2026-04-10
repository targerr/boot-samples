package com.example.datascope.security;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.datascope.config.DataScopeConfig;
import com.example.datascope.entity.User;
import com.example.datascope.service.DeptService;
import com.example.datascope.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 自定义用户详情服务
 * 实现 Spring Security 的 UserDetailsService 接口
 * 负责加载用户信息并构建数据权限对象
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private DataScopeConfig dataScopeConfig;

	/**
	 * 根据用户名加载用户详情
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 查询用户信息
		User user = userService.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在: " + username);
		}

		log.info("加载用户信息: username={}, deptId={}", user.getUsername(), user.getDeptId());

		// 构建用户角色（这里简化处理，实际应该从数据库查询）
		Set<String> roles = getUserRoles(user);

		// 构建数据权限对象
		DataScope dataScope = buildDataScope(user, roles);

		log.info("用户数据权限: isAll={}, deptIds={}, isSelf={}, scopeName={}, creatorName={}",
			dataScope.isAll(), dataScope.getDeptIds(), dataScope.isSelf(),
			dataScope.getScopeName(), dataScope.getCreatorName());

		// 返回自定义的 UserDetails
		return new CustomUserDetails(user, roles, dataScope);
	}

	/**
	 * 获取用户角色
	 * 这里简化处理，实际应该从数据库查询
	 */
	private Set<String> getUserRoles(User user) {
		// 根据用户名判断角色（演示用）
		String username = user.getUsername();

		if ("admin".equals(username)) {
			return Set.of("ROLE_ADMIN", "ROLE_USER");
		} else if ("manager".equals(username) || "sales_mgr".equals(username)) {
			return Set.of("ROLE_MANAGER", "ROLE_USER");
		} else {
			return Set.of("ROLE_USER");
		}
	}

	/**
	 * 构建数据权限对象
	 * 根据用户的最大权限角色来决定数据权限范围
	 * 从配置文件读取 scopeName 和 creatorName
	 */
	public DataScope buildDataScope(User user, Set<String> roles) {
		DataScope dataScope = new DataScope();
		dataScope.setUserId(user.getId());

		// 从配置文件读取字段名，实现灵活配置
		String scopeName = dataScopeConfig.getDefaultScopeName();
		String creatorName = dataScopeConfig.getDefaultCreatorName();

		dataScope.setScopeName(scopeName);
		dataScope.setCreatorName(creatorName);

		log.debug("使用配置字段名: scopeName={}, creatorName={}", scopeName, creatorName);

		// 根据角色决定数据权限
		if (roles.contains("ROLE_ADMIN")) {
			// 管理员：全部数据权限
			dataScope.setAll(true);
			log.info("管理员用户，拥有全部数据权限");
		} else if (roles.contains("ROLE_MANAGER")) {
			// 部门经理：本部门及所有子部门数据权限
			Set<Long> deptIds = deptService.getDeptAndAllChildIdsJavaRecursive(user.getDeptId());
			dataScope.getDeptIds().addAll(deptIds);
			log.info("部门经理用户，拥有部门数据权限: deptId={}, allDeptIds={}", user.getDeptId(), deptIds);
		} else {
			// 普通用户：只能看到自己创建的数据
			dataScope.setSelf(true);
			log.info("普通用户，拥有本人数据权限: userId={}", user.getId());
		}

		return dataScope;
	}

}
