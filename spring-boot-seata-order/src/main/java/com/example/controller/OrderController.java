package com.example.controller;

import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:32
 * @Classname OrderController
 * @Description
 */
@RestController
public class OrderController {

    @Resource(name="orderRestServiceImpl")
    private OrderService orderRestServiceImpl;

    @GetMapping("/create")
    public Boolean create(@RequestParam Integer count) {
        return orderRestServiceImpl.create(count);
    }
}
