package com.example.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:35
 * @Classname ResponseUtils
 * @Description
 */
public class ResponseUtils {


    /**
     * 发送文本，使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderText(HttpServletResponse response, String text) {
        render(response, "text/plain; charset=UTF-8", text);
    }

    /**
     * 发送Html, 使用UTF-8编码。
     *
     * @param response
     * @param text
     */
    public static void renderHtml(HttpServletResponse response, String html) {
        render(response, "text/html; charset=UTF-8", html);
    }

    /**
     * 发送json, 使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderJson(HttpServletResponse response, String json) {
        render(response, "applicatoin/json; charset=UTF-8", json);
    }

    /**
     * 发送xml, 使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderXml(HttpServletResponse response, String xml) {
        render(response, "application/xml;charset=UTF-8", xml);
    }

    /**
     * 发送内容
     *
     * @param response
     * @param contentType
     * @param text
     */
    public static void render(HttpServletResponse response, String contentType, String text) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
