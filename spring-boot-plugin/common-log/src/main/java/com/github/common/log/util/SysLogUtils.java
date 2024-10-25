
package com.github.common.log.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.github.common.log.holder.RequestHolder;
import com.github.common.log.pojo.LogLoginDo;
import com.github.common.log.pojo.LogOperateDo;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 系统日志工具类
 *
 * @author somewhere
 */
@UtilityClass
public class SysLogUtils {

    public LogOperateDo getSysLogOperate() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        LogOperateDo logOperate = new LogOperateDo();
        logOperate.setCreatedDate(LocalDateTime.now());
        logOperate.setUsername(getUsername());
        logOperate.setIpAddress(ServletUtil.getClientIP(request));
        logOperate.setIpLocation(NetworkUtil.getIpAddress(request));
        logOperate.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        UserAgent userAgent = UserAgentUtil.parse(logOperate.getUserAgent());
        logOperate.setBrowser(userAgent.getBrowser().getName());
        logOperate.setOs(userAgent.getOs().getName());
        logOperate.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        return logOperate;
    }

    public LogLoginDo getSysLogLogin() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String userAgentStr = request.getHeader(HttpHeaders.USER_AGENT);
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        String tempIp = ServletUtil.getClientIP(request);
        return LogLoginDo.builder()
                .ipAddress(tempIp)
                .ipLocation(NetworkUtil.getIpAddress(request))
                .username(getUsername())
                .createdBy(getUserId())
                .createdDate(LocalDateTime.now())
                .loginDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATE_FORMATTER))
                .userAgent(userAgentStr)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOs().getName())
                .requestUri(URLUtil.getPath(request.getRequestURI())).build();
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        return "";
    }

    /**
     * 获取用户Id
     *
     * @return userId
     */
    private Long getUserId() {
        return 0L;
    }

}
