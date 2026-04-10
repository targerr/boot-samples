package com.example.datascope.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体
 */
@Data
@TableName("sys_dept")
public class Dept {

	/**
	 * 部门ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 父部门ID
	 */
	private Long parentId;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdDate;

}
