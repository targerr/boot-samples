package com.example.controller;

import com.example.annotation.Permission;
import com.example.service.UserStatisticService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname TestController
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
@Autowired
private UserStatisticService userStatisticService;;
    @GetMapping("/permisson")
    @Permission
    public ResVo permission() {
        return ResultVoUtil.success();
    }

    @GetMapping("/per")
    @Permission
    public ResVo per() {
        return ResultVoUtil.success();
    }

    @GetMapping("/authority")
    @Permission
    public ResVo authority() {
        return ResultVoUtil.success();
    }

    @GetMapping("/onlineCnt")
    public ResVo onlineCnt(){
        return ResultVoUtil.success(userStatisticService.getOnlineUserCnt());
    }
}
