package com.example.datascope.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("sys_user")
public class User {

	/**
	 * 用户ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 创建者ID
	 */
	private Long createdBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdDate;

	/**
	 * 删除标记（0-正常，1-删除）
	 */
	private Integer delFlag;

	/**
	 * 部门名称（非数据库字段）
	 */
	@TableField(exist = false)
	private String deptName;

}
