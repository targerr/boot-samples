package com.example.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: wgs
 * @Date 2022/6/7 09:50
 * @Classname PaymentForm
 * @Description
 */
@Data
public class PaymentForm {
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;
    @NotNull(message = "支付方式不能为空")
    private String payType;
    private String openId ;
}
