package com.example.controller;

import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.service.SegmentService;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdContoller {

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private SnowflakeService snowflakeService;

    @GetMapping("/segment")
    public Long segment() {
        return segmentService.getId("leaf-segment-test").getId();
    }

    @GetMapping("/snowflake")
    public Result snowflake() {
        return snowflakeService.getId("example");
    }
}
