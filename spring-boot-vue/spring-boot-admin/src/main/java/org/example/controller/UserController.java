package org.example.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.User;
import org.example.param.LoginParam;
import org.example.param.UserParam;
import org.example.result.Result;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/page")
    public Result getBlogPage(Page<User> pageParam, UserParam userParam) {
        Page<User> page = userService.lambdaQuery()
                .like(StrUtil.isNotBlank(userParam.getCode()), User::getCode,userParam.getCode())
                .like(StrUtil.isNotBlank(userParam.getUsername()), User::getUsername,userParam.getUsername())
                .like(StrUtil.isNotBlank(userParam.getName()), User::getName,userParam.getName())
                .page(pageParam);
        return Result.success(page);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginParam loginParam) {
        List<User> list = userService.lambdaQuery().eq(User::getUsername, loginParam.getUsername()).list();
        if (CollectionUtil.isEmpty(list)) {
            throw new RuntimeException("用户不存在！");
        }
        User selectUser = userService.lambdaQuery().eq(User::getUsername, loginParam.getUsername())
                .eq(User::getPassword, loginParam.getPassword()).one();
        if (null == selectUser) {
            throw new RuntimeException("密码不正确！");
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userInfo", selectUser);
            resultMap.put("token", "123456");
            return Result.success(resultMap);
        }
    }
}
