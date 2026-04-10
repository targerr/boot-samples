package com.example.datascope.controller;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.datascope.entity.User;
import com.example.datascope.security.CustomUserDetails;
import com.example.datascope.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 首页
	 */
	@GetMapping("/home")
	public ApiResult<HomeData> home() {
		CustomUserDetails currentUser = getCurrentUser();

		HomeData homeData = new HomeData();
		homeData.setUsername(currentUser.getUsername());
		homeData.setRealName(currentUser.getRealName());
		homeData.setDeptId(currentUser.getDeptId());
		homeData.setRoles(currentUser.getRoles());
		homeData.setDataScopeInfo(buildDataScopeInfo(currentUser.getDataScope()));

		return ApiResult.success(homeData);
	}



	@GetMapping("/queryUsers")
	public ApiResult<List<User>> queryUsers() {
		log.info("查询用户列表（AOP带数据权限过滤）");
		List<User> users = userService.queryUsers();
		return ApiResult.success(users);
	}
	/**
	 * 查询用户列表（带数据权限过滤）
	 */
	@GetMapping("/users")
	public ApiResult<List<User>> getUsers() {
		log.info("查询用户列表（带数据权限过滤）");
		List<User> users = userService.queryAllUsers();
		return ApiResult.success(users);
	}

	/**
	 * 查询用户列表（不过滤）
	 * 仅管理员可用
	 */
	@GetMapping("/admin/users/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResult<List<User>> getAllUsers() {
		log.info("查询用户列表（管理员，不过滤）");
		List<User> users = userService.queryAllUsersWithoutFilter();
		return ApiResult.success(users);
	}

	/**
	 * 测试不同的数据权限
	 */
	@GetMapping("/test/datascope")
	public ApiResult<TestResult> testDataScope() {
		CustomUserDetails currentUser = getCurrentUser();
		DataScope dataScope = currentUser.getDataScope();

		TestResult result = new TestResult();
		result.setCurrentUser(currentUser.getUsername());
		result.setDataScopeType(getDataScopeType(dataScope));

		if (dataScope.isAll()) {
			result.setDescription("您拥有全部数据权限，可以查看所有数据");
		} else if (dataScope.isSelf()) {
			result.setDescription("您只能查看自己创建的数据");
		} else if (!dataScope.getDeptIds().isEmpty()) {
			result.setDescription("您可以查看以下部门的数据: " + dataScope.getDeptIds());
		} else {
			result.setDescription("无数据权限限制");
		}

		return ApiResult.success(result);
	}

	/**
	 * 获取当前登录用户
	 */
	private CustomUserDetails getCurrentUser() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof CustomUserDetails) {
				return (CustomUserDetails) principal;
			}
		} catch (Exception e) {
			log.warn("获取当前用户失败: {}", e.getMessage());
		}
		return null;
	}

	/**
	 * 构建数据权限信息
	 */
	private String buildDataScopeInfo(DataScope dataScope) {
		if (dataScope.isAll()) {
			return "全部数据权限";
		} else if (dataScope.isSelf()) {
			return "本人数据权限";
		} else if (!dataScope.getDeptIds().isEmpty()) {
			return "部门数据权限: " + dataScope.getDeptIds();
		} else {
			return "未设置数据权限";
		}
	}

	/**
	 * 获取数据权限类型描述
	 */
	private String getDataScopeType(DataScope dataScope) {
		if (dataScope.isAll()) {
			return "ALL";
		} else if (dataScope.isSelf()) {
			return "SELF";
		} else if (!dataScope.getDeptIds().isEmpty()) {
			return "DEPT";
		} else {
			return "NONE";
		}
	}

	/**
	 * API 响应结果
	 */
	@Data
	@AllArgsConstructor
	static class ApiResult<T> {
		private int code;
		private String message;
		private T data;

		static <T> ApiResult<T> success(T data) {
			return new ApiResult<>(200, "success", data);
		}
	}

	/**
	 * 首页数据
	 */
	@Data
	static class HomeData {
		private String username;
		private String realName;
		private Long deptId;
		private java.util.Set<String> roles;
		private String dataScopeInfo;
	}

	/**
	 * 测试结果
	 */
	@Data
	static class TestResult {
		private String currentUser;
		private String dataScopeType;
		private String description;
	}

}
