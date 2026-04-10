package com.example.datascope.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("biz_order")
public class Order {

	/**
	 * 订单ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 订单编号
	 */
	private String orderNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 订单金额
	 */
	private BigDecimal amount;

	/**
	 * 所属部门ID
	 */
	private Long deptId;

	/**
	 * 创建者ID（销售人员）
	 */
	private Long createdBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdDate;

	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
