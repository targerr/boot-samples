package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.req.UserReq;
import com.example.service.SysUserService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:05
 * @Classname UserController
 * @Description
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ResVo login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        UserInfoDTO userInfoDTO = sysUserService.login(username, password);
        return ResultVoUtil.success(userInfoDTO);
    }

    @PostMapping("/register")
    public ResVo<Boolean> register(@Validated  @RequestBody UserReq userReq) {
        String token = sysUserService.register(userReq);

        return ResultVoUtil.success(true);

    }

}
