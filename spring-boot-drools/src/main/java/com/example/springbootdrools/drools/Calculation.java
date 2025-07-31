package com.example.springbootdrools.drools;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: wgs
 * @Date 2025/5/20 16:07
 * @Classname Calculation
 * @Description
 */
@Data
public class Calculation {
    private BigDecimal wage;//税前工资
    private BigDecimal wagemore;//应纳税所得额
    private BigDecimal cess;//税率
    private BigDecimal preminus;//速算扣除数
    private BigDecimal wageminus;//扣税额
    private BigDecimal actualwage;//税后工资

    /*起征点*/
   private BigDecimal threshold;

}