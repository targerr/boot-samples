/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.controller;

import cn.hutool.json.JSONUtil;
import com.example.entity.User;
import com.example.mdc.MdcDot;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
public class UserController {

    // http://127.0.0.1:9527/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    @MdcDot(bizCode = "#articleId")
    public String hello(@RequestParam(value = "articleId") Long articleId, @RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + articleId;
    }


    @RequestMapping("/user")
    @ResponseBody
    @MdcDot(bizCode = "#user.articleId")
    public String saveUser1(@RequestBody User user) {
        return JSONUtil.parse(user).toString();
    }

}
