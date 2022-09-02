package com.example.share.controller;

import com.example.share.dto.share.ShareDTO;
import com.example.share.service.IShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private IShareService service;

    @GetMapping("/findById")
    public ShareDTO findById(String id) {
        return service.findById(id);
    }


    @GetMapping("/{id}")
    public ShareDTO details(@PathVariable("id") String id) {
        return service.detail(id);
    }
}
