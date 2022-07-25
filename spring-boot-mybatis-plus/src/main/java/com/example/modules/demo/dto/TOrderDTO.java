package com.example.modules.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 *
 * @author wgs sunlightcs@gmail.com
 * @since 1.0.0 2022-07-25
 */
@Data
@ApiModel(value = "")
public class TOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long orderId;

	@ApiModelProperty(value = "")
	private String orderNo;

	@ApiModelProperty(value = "")
	private Integer orderStatus;

	@ApiModelProperty(value = "")
	private String userid;

	@ApiModelProperty(value = "")
	private String recvName;

	@ApiModelProperty(value = "")
	private String recvAddress;

	@ApiModelProperty(value = "")
	private String recvMobile;

	@ApiModelProperty(value = "")
	private Float postage;

	@ApiModelProperty(value = "")
	private Float amout;

	@ApiModelProperty(value = "")
	private Date createTime;


}