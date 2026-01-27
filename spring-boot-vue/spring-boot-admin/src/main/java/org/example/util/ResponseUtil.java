package org.example.util;

import org.example.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public static void out(HttpServletResponse response, Result result) {
        try {
            response.setContentType("application/json;utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
