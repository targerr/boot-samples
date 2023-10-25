package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.req.UserReq;
import com.example.service.LoginOutService;
import com.example.service.SysUserService;
import com.example.util.SessionUtil;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:05
 * @Classname UserController
 * @Description
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户控制器")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    @Operation(summary = "用户登录",description = "登录")
    @Parameters({
            @Parameter(name = "username",example = "tom",description = "用户名",required = true,in = ParameterIn.QUERY),
            @Parameter(name = "password",description = "密码",required = true,in=ParameterIn.QUERY)
    })
    public ResVo login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password,
                       HttpServletResponse response) {
        UserInfoDTO userInfoDTO = sysUserService.login(username, password);
        if (Objects.nonNull(userInfoDTO)){
            response.addCookie(SessionUtil.newCookie(LoginOutService.SESSION_KEY, userInfoDTO.getToken()));
        }
        return ResultVoUtil.success(userInfoDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "创建用户",description = "根据姓名创建用户")
    public ResVo<Boolean> register(@Validated  @RequestBody UserReq userReq) {
        String token = sysUserService.register(userReq);

        return ResultVoUtil.success(true);

    }

}
