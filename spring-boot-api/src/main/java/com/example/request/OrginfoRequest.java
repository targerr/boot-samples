package com.example.request;


import com.example.annotation.Url;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 业务单位注册信息 实体类
 *
 * @author sa
 * @since 2025-02-18
 */
@Url("/zc/orginfo")
@Data
public class OrginfoRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 行唯一标识
	 */
	private String rowGuid;
	/**
	 * 单位唯一标识
	 */
	private String danWeiGuid;
	/**
	 * 单位名称
	 */
	private String danWeiName;
	/**
	 * 单位类别
	 */
	private String danWeiType;
	/**
	 * 统一社会信用代码（身份证）
	 */
	private String unitOrgNum;
	/**
	 * 注册地代码
	 */
	private String areaCode;
	/**
	 * 注册地
	 */
	private String areaName;
	/**
	 * 注册资本（元）
	 */
	private BigDecimal registMoney;
	/**
	 * 交易平台名称
	 */
	private String platformName;
	/**
	 * 交易平台标识码
	 */
	private String platformCode;
}
