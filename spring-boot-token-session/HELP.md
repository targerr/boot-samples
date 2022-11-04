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

### session 机制:

- 采用的是在服务器端保持Http状态信息的方案。
- 必须采用一种机制来唯一标识一个用户，同时记录该用户的状态。
  于是就引入了第一种机制:Cookie机制;那么第二种就是Session机制。
  而Session是存储在服务器端。

### 图解

![image.png](https://upload-images.jianshu.io/upload_images/4994935-d741c134da1e15cd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/4994935-62945cdb2b260379.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/4994935-570ae403a6b354d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 会话固定攻击

- 什么是会话固定攻击？

> 会话固定攻击（session fixation attack）是利用应用系统在服务器的会话ID固定不变机制，借助他人用相同的会话ID获取认证和授权，然后利用该会话ID劫持他人的会话以成功冒充他人，造成会话固定攻击。

### 攻击修复

- 攻击分析

> 攻击的整个过程，会话ID是没变过的，所以导致此漏洞。

- 修复
- 每次登录后都重置会话ID，并生成一个新的会话ID，这样攻击者就无法用自己的会话ID来劫持会话，核心代码如下

```
        User user = userService.findById(id);
        //若存在会话则返回该会话，否则返回NULL
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        //request.getSession(true)：若存在会话则返回该会话，否则新建一个会话。
        request.getSession(true).setAttribute("user", user);
        return user;
```

#### [JWT 实现登录认证 + Token 自动续期方案👀️ ](https://mp.weixin.qq.com/s/QztlrGR5XlkGvgfSCGAB_A)
