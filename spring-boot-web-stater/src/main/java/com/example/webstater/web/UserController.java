package com.example.webstater.web;

import com.example.webstater.result.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/10/18 17:15
 * @Classname UserController
 * @Description
 */

@RestController
public class UserController {
    @RequestMapping("/user")
    public User user() {
        return new User("路人", 30);
    }

    @RequestMapping("/user/list")
    public List<User> list() {
        List<User> result = Arrays.asList(new User("SpringMVC系列", 3), new User("SpringBoot系列", 2));
        return result;
    }

    @RequestMapping("/user/m1")
    public String m1() {
        return "ok";
    }

    @RequestMapping("/user/m2")
    public Integer m2() {
        return 1;
    }

    @RequestMapping("/user/m3")
    public ApiResult<?> m3() {
        final ApiResult<?> ok = ApiResult.success("ok");
        return ok;
    }

    public static class User {
        private String name;
        private Integer age;

        public User() {
        }

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}