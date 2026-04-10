package com.example.datascope.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库初始化控制器
 * 用于初始化演示数据
 */
@Slf4j
@RestController
@RequestMapping("/api/init")
public class InitController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 初始化演示用户数据
	 * POST /api/init/users
	 */
	@PostMapping("/users")
	public ApiResult<Map<String, Object>> initUsers() {
		log.info("开始初始化演示用户数据...");

		try {
			Map<String, Object> result = new HashMap<>();

			// 生成密码哈希
			String adminHash = passwordEncoder.encode("admin123");
			String mgrHash = passwordEncoder.encode("mgr123");
			String userHash = passwordEncoder.encode("user123");

			result.put("admin_password_hash", adminHash);
			result.put("manager_password_hash", mgrHash);
			result.put("user_password_hash", userHash);

			// 检查用户是否已存在
			Long count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM sys_user WHERE username IN ('admin', 'manager', 'user1')",
				Long.class
			);

			if (count != null && count > 0) {
				// 更新现有用户的密码
				log.info("用户已存在，更新密码...");

				jdbcTemplate.update("UPDATE sys_user SET password = ? WHERE username = ?", adminHash, "admin");
				jdbcTemplate.update("UPDATE sys_user SET password = ? WHERE username IN ('manager', 'sales_mgr')", mgrHash);
				jdbcTemplate.update("UPDATE sys_user SET password = ? WHERE username LIKE 'user%'", userHash);

				result.put("action", "updated");
				result.put("message", "用户密码已更新");
			} else {
				// 插入新用户
				log.info("插入新用户...");

				// 先插入部门（如果不存在）
				jdbcTemplate.update("INSERT IGNORE INTO sys_dept (id, name, parent_id, sort) VALUES " +
					"(1, '总公司', 0, 1), " +
					"(2, '研发部', 1, 2), " +
					"(3, '销售部', 1, 3), " +
					"(5, '研发一组', 2, 5), " +
					"(6, '研发二组', 2, 6), " +
					"(7, '销售一组', 3, 7), " +
					"(8, '销售二组', 3, 8)");

				// 插入用户
				jdbcTemplate.update("INSERT INTO sys_user (id, username, password, real_name, dept_id, created_by) VALUES " +
					"(1, ?, ?, '系统管理员', 1, NULL)", "admin", adminHash);

				jdbcTemplate.update("INSERT INTO sys_user (id, username, password, real_name, dept_id, created_by) VALUES " +
					"(2, ?, ?, '研发部经理', 2, 1), " +
					"(3, ?, ?, '销售部经理', 3, 1)", "manager", mgrHash, "sales_mgr", mgrHash);

				jdbcTemplate.update("INSERT INTO sys_user (id, username, password, real_name, dept_id, created_by) VALUES " +
					"(4, ?, ?, '研发工程师A', 5, 1), " +
					"(5, ?, ?, '研发工程师B', 6, 1), " +
					"(6, ?, ?, '销售代表A', 7, 1), " +
					"(7, ?, ?, '销售代表B', 8, 1)",
					"user1", userHash, "user2", userHash, "user3", userHash, "user4", userHash);

				result.put("action", "inserted");
				result.put("message", "新用户已插入");
			}

			// 验证插入结果
			Map<String, Object> users = new HashMap<>();
			jdbcTemplate.query("SELECT username, real_name, dept_id FROM sys_user WHERE del_flag = 0 ORDER BY id",
				rs -> {
					users.put(rs.getString("username"),
						Map.of("realName", rs.getString("real_name"),
							"deptId", rs.getLong("dept_id")));
				});
			result.put("users", users);

			log.info("演示用户数据初始化完成!");
			log.info("测试账号:");
			log.info("  - admin / admin123 (管理员)");
			log.info("  - manager / mgr123 (部门经理)");
			log.info("  - user1 / user123 (普通用户)");

			return ApiResult.success(result, "初始化成功");

		} catch (Exception e) {
			log.error("初始化失败: {}", e.getMessage(), e);
			return ApiResult.error("初始化失败: " + e.getMessage());
		}
	}

	@Data
	@AllArgsConstructor
	static class ApiResult<T> {
		private int code;
		private String message;
		private T data;

		static <T> ApiResult<T> success(T data, String message) {
			return new ApiResult<>(200, message, data);
		}

		static <T> ApiResult<T> error(String message) {
			return new ApiResult<>(500, message, null);
		}
	}
}
