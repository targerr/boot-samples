package com.example.datascope.controller;

import com.example.datascope.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * 登录接口（JSON 格式）
	 * POST /api/auth/login
	 * Body: {"username":"admin","password":"admin123"}
	 *
	 * 登录成功后，会自动创建 Session，后续请求携带 JSESSIONID Cookie 即可
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResult<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
		log.info("用户登录请求: username={}", loginRequest.getUsername());

		try {
			// 1. 使用 AuthenticationManager 进行认证（会调用 CustomUserDetailsService）
			// 这会验证密码并创建认证的 Session
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(),
					loginRequest.getPassword()
				)
			);

			// 2. 设置认证信息到 SecurityContext（会自动保存到 Session）
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 3. 获取用户详情
			Object principal = authentication.getPrincipal();
			if (principal instanceof CustomUserDetails) {
				CustomUserDetails userDetails = (CustomUserDetails) principal;

				// 4. 返回登录成功信息
				LoginResponse response = new LoginResponse();
				response.setUsername(userDetails.getUsername());
				response.setRealName(userDetails.getRealName());
				response.setDeptId(userDetails.getDeptId());
				response.setRoles(userDetails.getRoles());
				response.setDataScopeInfo(buildDataScopeInfo(userDetails.getDataScope()));

				log.info("用户登录成功: username={}, sessionId={}",
					loginRequest.getUsername(),
					authentication.getDetails());

				return ResponseEntity.ok(ApiResult.success(response, "登录成功"));
			}

			return ResponseEntity.ok(ApiResult.error("登录失败：无法获取用户信息"));

		} catch (Exception e) {
			log.warn("登录失败: username={}, error={}", loginRequest.getUsername(), e.getMessage());
			return ResponseEntity.ok(ApiResult.error("用户名或密码错误"));
		}
	}

	/**
	 * 构建数据权限信息
	 */
	private String buildDataScopeInfo(com.albedo.java.plugins.datascope.model.DataScope dataScope) {
		if (dataScope == null) {
			return "未设置数据权限";
		}
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
	 * 获取当前用户信息
	 * GET /api/auth/current
	 *
	 * 需要携带登录后的 Cookie (JSESSIONID)
	 */
	@GetMapping("/current")
	public ResponseEntity<ApiResult<CurrentUserInfo>> getCurrentUser() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated() ||
				"anonymousUser".equals(authentication.getPrincipal())) {
				log.warn("获取当前用户失败：未登录");
				return ResponseEntity.status(401).body(ApiResult.error("未登录，请先登录"));
			}

			Object principal = authentication.getPrincipal();
			CurrentUserInfo userInfo = new CurrentUserInfo();

			if (principal instanceof CustomUserDetails) {
				CustomUserDetails userDetails = (CustomUserDetails) principal;
				userInfo.setUsername(userDetails.getUsername());
				userInfo.setRealName(userDetails.getRealName());
				userInfo.setDeptId(userDetails.getDeptId());
				userInfo.setRoles(userDetails.getRoles());

				// 数据权限信息
				userInfo.setDataScopeInfo(buildDataScopeInfo(userDetails.getDataScope()));

				log.info("获取当前用户成功: username={}", userDetails.getUsername());
				return ResponseEntity.ok(ApiResult.success(userInfo));
			}

			userInfo.setUsername(authentication.getName());
			return ResponseEntity.ok(ApiResult.success(userInfo));

		} catch (Exception e) {
			log.error("获取当前用户信息失败: {}", e.getMessage(), e);
			return ResponseEntity.ok(ApiResult.error("获取用户信息失败"));
		}
	}

	/**
	 * 登出
	 * POST /api/auth/logout
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResult<Void>> logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			log.info("用户登出: username={}", authentication.getName());
		}
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok(ApiResult.success(null, "登出成功"));
	}

	// ========== DTO 类 ==========

	/**
	 * 登录请求
	 */
	@Data
	static class LoginRequest {
		private String username;
		private String password;
	}

	/**
	 * 登录响应
	 */
	@Data
	static class LoginResponse {
		private String username;
		private String realName;
		private Long deptId;
		private java.util.Set<String> roles;
		private String dataScopeInfo;
	}

	/**
	 * 当前用户信息
	 */
	@Data
	static class CurrentUserInfo {
		private String username;
		private String realName;
		private Long deptId;
		private java.util.Set<String> roles;
		private String dataScopeInfo;
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

		static <T> ApiResult<T> success(T data, String message) {
			return new ApiResult<>(200, message, data);
		}

		static <T> ApiResult<T> error(String message) {
			return new ApiResult<>(401, message, null);
		}
	}
}
