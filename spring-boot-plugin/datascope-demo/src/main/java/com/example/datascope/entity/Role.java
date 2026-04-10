package com.example.datascope.entity;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色实体
 */
@Data
@TableName("sys_role")
public class Role {

	/**
	 * 角色ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色编码
	 */
	private String code;

	/**
	 * 数据权限类型
	 */
	private DataScopeType dataScopeType;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdDate;

}
