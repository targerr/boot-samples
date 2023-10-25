package com.example.hook.filter;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname ReqRecordFilter
 * @since 1.0.0
 *  * 1. 请求参数日志输出过滤器
 *  * 2. 判断用户是否登录
 */

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.context.ReqInfoContext;
import com.example.globall.GlobalInitService;
import com.example.service.LoginOutService;
import com.example.util.CrossUtil;
import com.example.util.IpUtil;
import com.example.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "reqRecordFilter", asyncSupported = true)
public class ReqRecordFilter implements Filter {

    @Resource
    private GlobalInitService globalInitService;

    @Override
    public void init(FilterConfig filterConfig)  {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = null;
        try {
            request = this.initReqInfo((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            CrossUtil.buildCors(request, (HttpServletResponse) servletResponse);
            filterChain.doFilter(request, servletResponse);
        } finally {
            buildRequestLog(ReqInfoContext.getReqInfo(), request, System.currentTimeMillis() - start);
            ReqInfoContext.clear();
        }
    }


    private HttpServletRequest initReqInfo(HttpServletRequest request, HttpServletResponse response) {
        if (isStaticURI(request)) {
            // 静态资源直接放行
            return request;
        }

        try {

            // 手动写入一个session，借助 OnlineUserCountListener 实现在线人数实时统计
            request.getSession().setAttribute("latestVisit", System.currentTimeMillis());

            ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
            reqInfo.setHost(request.getHeader("host"));
            reqInfo.setPath(request.getPathInfo());
            if (reqInfo.getPath() == null) {
                String url = request.getRequestURI();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                reqInfo.setPath(url);
            }
            reqInfo.setReferer(request.getHeader("referer"));
            reqInfo.setClientIp(IpUtil.getClientIp(request));
            reqInfo.setUserAgent(request.getHeader("User-Agent"));
            reqInfo.setDeviceId(getOrInitDeviceId(request, response));

            // 通过header取用户信息
            String token = request.getHeader(LoginOutService.USER_TOKEN);
            // 初始化登录信息
            globalInitService.initLoginUser(reqInfo);

            request = this.wrapperRequest(request, reqInfo);

            ReqInfoContext.addReqInfo(reqInfo);

            log.info("reqInfo:{}", JSONObject.toJSONString(reqInfo));
            // 更新uv/pv计数
          //  AsyncUtil.execute(() -> SpringUtil.getBean(SitemapServiceImpl.class).saveVisitInfo(reqInfo.getClientIp(), reqInfo.getPath()));


        } catch (Exception e) {
            log.error("init reqInfo error!", e);
        }

        return request;
    }

    private void buildRequestLog(ReqInfoContext.ReqInfo req, HttpServletRequest request, long costTime) {
        if (req == null || isStaticURI(request)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        if (StringUtils.isNotBlank(req.getReferer())) {
            msg.append("referer=").append(URLDecoder.decode(req.getReferer())).append("; ");
        }
        msg.append("remoteIp=").append(req.getClientIp());
        msg.append("; agent=").append(req.getUserAgent());

        if (req.getUserId() != null) {
            // 打印用户信息
            msg.append("; user=").append(req.getUserId());
        }

        msg.append("; uri=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(URLDecoder.decode(request.getQueryString()));
        }

        msg.append("; payload=").append(req.getPayload());
        msg.append("; cost=").append(costTime);
        log.info("请求日志信息: {}", msg);

        // 保存请求计数
       // statisticsSettingService.saveRequestCount(req.getClientIp());
    }


    private HttpServletRequest wrapperRequest(HttpServletRequest request, ReqInfoContext.ReqInfo reqInfo) {
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            return request;
        }

        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        reqInfo.setPayload(requestWrapper.getBodyString());
        return requestWrapper;
    }

    private boolean isStaticURI(HttpServletRequest request) {
        return request == null
                || request.getRequestURI().endsWith("css")
                || request.getRequestURI().endsWith("js")
                || request.getRequestURI().endsWith("png")
                || request.getRequestURI().endsWith("ico")
                || request.getRequestURI().endsWith("svg")
                || request.getRequestURI().endsWith("min.js.map")
                || request.getRequestURI().endsWith("min.css.map");
    }


    /**
     * 初始化设备id
     *
     * @return
     */
    private String getOrInitDeviceId(HttpServletRequest request, HttpServletResponse response) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId) && !"null".equalsIgnoreCase(deviceId)) {
            return deviceId;
        }

        Cookie device = SessionUtil.findCookieByName(request, LoginOutService.USER_DEVICE_KEY);
        if (device == null) {
            deviceId = UUID.randomUUID().toString();
            if (response != null) {
                response.addCookie(SessionUtil.newCookie(LoginOutService.USER_DEVICE_KEY, deviceId));
            }
            return deviceId;
        }
        return device.getValue();
    }

    @Override
    public void destroy() {
    }
}
