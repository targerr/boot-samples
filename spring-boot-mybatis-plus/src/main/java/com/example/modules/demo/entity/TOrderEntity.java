package com.example.modules.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author wgs sunlightcs@gmail.com
 * @since 1.0.0 2022-07-25
 */
@Data
@TableName("t_order")
public class TOrderEntity {

    /**
     * 
     */
	private Long orderId;
    /**
     * 
     */
	private String orderNo;
    /**
     * 
     */
	private Integer orderStatus;
    /**
     * 
     */
	private String userid;
    /**
     * 
     */
	private String recvName;
    /**
     * 
     */
	private String recvAddress;
    /**
     * 
     */
	private String recvMobile;
    /**
     * 
     */
	private Float postage;
    /**
     * 
     */
	private Float amout;
    /**
     * 
     */
	private Date createTime;
}