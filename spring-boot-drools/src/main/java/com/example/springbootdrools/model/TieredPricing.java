package com.example.springbootdrools.model;

import lombok.Data;

@Data
public class TieredPricing {
    private Long startTime; //起始时间
    private Long endTime; //结束时间
    private Integer duration; //停车时长
    private Float cost; //费用
}
