### 整合JWT

##### 示例

第一步：创建starter工程spring-boot-token-session

第二步：测试

~~~java

package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: wgs
 * @Date 2022/5/13 10:30
 * @Classname SessionController
 * @Description
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    @GetMapping("/login")
    public String login(String name, String age, HttpSession session) {
        // 业务验证......
        session.setAttribute("user", name + ":" + age);

        return "login success!";
    }

    @GetMapping("/info")
    public String info(HttpSession session) {
        final Object user = session.getAttribute("user");

        return user.toString();
    }
}


~~~

###图示:

#### 登录 响应头设置
* ![image.png](https://upload-images.jianshu.io/upload_images/4994935-42622cc6705e82ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 获取 请求头带入
* ![image.png](https://upload-images.jianshu.io/upload_images/4994935-af1a284d92528d26.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 使用localhost代替127.0.0.1 测试跨域
* ![image.png](https://upload-images.jianshu.io/upload_images/4994935-f7c6ae508cc37464.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 流程图
* ![image.png](https://upload-images.jianshu.io/upload_images/4994935-0e424dfff6599e96.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
