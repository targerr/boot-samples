package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.bo.TestDemoBo;
import com.example.utils.ValidatorUtils;
import com.example.validate.AddGroup;
import com.example.validate.EditGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wgs
 * @Date 2022/7/4 16:39
 * @Classname ValidatorController
 * @Description
 */
@RestController
@RequestMapping("/validator")
public class ValidatorController {

    @PostMapping("/add")
    public Dict add(@RequestBody TestDemoBo bo) {
        // 使用校验工具校验 @Validated(AddGroup.class) 注解
        // 用于非 controller 的地方校验对象
        ValidatorUtils.validate(bo, AddGroup.class);
        return Dict.create().set("msg", "success");
    }

    @PutMapping("/edit")
    public Dict edit(@Validated(EditGroup.class) @RequestBody TestDemoBo bo){
        return Dict.create().set("msg", "success");
    }
}
