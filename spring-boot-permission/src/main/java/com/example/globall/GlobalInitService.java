package com.example.globall;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.example.context.ReqInfoContext;
import com.example.dto.BaseUserInfoDTO;
import com.example.service.LoginOutService;
import com.example.service.SysUserService;
import com.example.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname GlobalInitService
 * @since 1.0.0
 */
@Slf4j
@Service
public class GlobalInitService {
    @Autowired
    private SysUserService userService;

    /**
     * 初始化用户信息
     *
     * @param reqInfo
     */
    public void initLoginUser(ReqInfoContext.ReqInfo reqInfo) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = ServletUtil.getHeader(request, LoginOutService.USER_TOKEN, CharsetUtil.UTF_8);
        if (StringUtils.isBlank(token)) {
            return;
        }
        Optional.ofNullable(SessionUtil.findCookieByName(request, LoginOutService.SESSION_KEY))
                .ifPresent(cookie -> initLoginUser(cookie.getValue(), reqInfo));
    }

    public void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo) {
        BaseUserInfoDTO user = userService.getAndUpdateUserIpInfoBySessionId(session, null);
        reqInfo.setSession(session);
        if (user != null) {
            reqInfo.setUserId(user.getId());
            reqInfo.setUser(user);
        }
    }
}
