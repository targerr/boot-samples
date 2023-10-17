package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.service.SysUserService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResVo<UserInfoDTO> login(@RequestParam(name="username") String username ,@RequestParam(name="password") String password){

    UserInfoDTO userInfoDTO = sysUserService.login(username, password);;
    return ResultVoUtil.success(userInfoDTO);
    }
}
