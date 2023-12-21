package com.example.img;

/**
 * @Author: wgs
 * @Date 2023/12/20
 * @Classname EasyExcelUtil
 * @since 1.0.0
 */
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author songwp
 * @since 2023-04-20 14:04:42
 */
@Slf4j
public class EasyExcelUtil {

    /**
     * 导出excel
     *
     * @param response          响应实体类
     * @param sheetName         sheet页签名称
     * @param clazz             模板的字节码对象
     * @param list              需要导出的数据列表

     */
    public static void exportExcel(HttpServletResponse response,
                                   String sheetName,
                                   Class clazz,
                                   List<?> list)  {
        try {
            String fileName = URLEncoder.encode(sheetName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz).autoCloseStream(Boolean.TRUE).sheet("sheet")
                    .doWrite(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
    }
}