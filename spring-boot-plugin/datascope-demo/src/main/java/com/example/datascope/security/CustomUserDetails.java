package com.example.datascope.security;

import com.albedo.java.plugins.datascope.aspect.DataScopeAspect;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.example.datascope.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义用户详情
 * 实现 Spring Security 的 UserDetails 接口
 * 实现 DataScopeAspect.DataScopeProvider 接口以支持 AOP 数据权限
 * 并携带数据权限对象
 */
@Getter
public class CustomUserDetails implements UserDetails {

	/**
	 * 用户ID
	 */
	private final Long id;

	/**
	 * 用户名
	 */
	private final String username;

	/**
	 * 密码
	 */
	private final String password;

	/**
	 * 真实姓名
	 */
	private final String realName;

	/**
	 * 部门ID
	 */
	private final Long deptId;

	/**
	 * 角色集合
	 */
	private final Set<String> roles;

	/**
	 * 数据权限对象
	 */
	private final DataScope dataScope;

	/**
	 * 构造函数
	 */
	public CustomUserDetails(User user, Set<String> roles, DataScope dataScope) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.realName = user.getRealName();
		this.deptId = user.getDeptId();
		this.roles = roles;
		this.dataScope = dataScope;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 将角色转换为 GrantedAuthority
		return roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

//	/**
//	 * 实现 DataScopeProvider 接口
//	 * 提供数据权限对象给 AOP 切面使用
//	 */
//	@Override
//	public DataScope getDataScope() {
//		return this.dataScope;
//	}

}
