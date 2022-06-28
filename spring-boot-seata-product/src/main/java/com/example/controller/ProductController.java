package com.example.controller;

import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:18
 * @Classname ProductController
 * @Description
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/deduct")
    public Boolean deduct(@RequestParam Long productId,
                          @RequestParam Integer count) {
        productService.deduct(productId, count);
        return true;
    }
}