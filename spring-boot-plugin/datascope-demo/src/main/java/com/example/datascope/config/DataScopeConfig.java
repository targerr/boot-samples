package com.example.datascope.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据权限配置属性
 * 从 application.yml 中读取 albedo.datascope 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "albedo.datascope")
public class DataScopeConfig {

	/**
	 * 是否启用数据权限
	 */
	private Boolean enabled = true;

	/**
	 * 默认权限字段名（部门ID字段）
	 */
	private String defaultScopeName = "dept_id";

	/**
	 * 默认创建者字段名
	 */
	private String defaultCreatorName = "created_by";

	/**
	 * 是否启用 SQL 日志
	 */
	private Boolean sqlLogEnabled = false;

	/**
	 * 是否启用 AOP
	 */
	private Boolean aopEnabled = false;
}
