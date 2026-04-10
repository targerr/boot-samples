package com.example.datascope.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码编码工具
 * 用于生成 BCrypt 密码哈希
 */
public class PasswordEncoderUtil {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public static void main(String[] args) {
		// 生成演示账号的密码哈希
		String[] passwords = {"admin123", "mgr123", "user123", "root123456"};

		System.out.println("BCrypt 密码哈希:");
		System.out.println("==================");

		for (String password : passwords) {
			String hash = encoder.encode(password);
			System.out.println(password + " -> " + hash);
		}

		System.out.println("\n验证密码:");
		System.out.println("==================");

		// 验证示例
		String adminHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi";
		System.out.println("验证 admin123 (旧哈希): " + encoder.matches("admin123", adminHash));

		String newHash = encoder.encode("admin123");
		System.out.println("验证 admin123 (新哈希): " + encoder.matches("admin123", newHash));
	}
}
