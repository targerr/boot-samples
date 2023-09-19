package com.example;

import cn.hutool.core.util.IdUtil;
import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Author: wgs
 * @Date 2023/8/30 15:52
 * @Classname Excel2Pdf
 * @Description
 */
public class Excel2Pdf {

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Excel2Pdf.class.getClassLoader().getResourceAsStream("license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void excel2pdf(String sourceFilePath,String desFilePath) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return;
        }
        try {

            File pdfFile = new File(desFilePath);// 输出路径
            Workbook wb = new Workbook(sourceFilePath);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            fileOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        String sourceFilePath = "/Users/wanggaoshuai/Documents/板桥行政村返乡人员排查登记表.xlsx";
        String desFilePath = "/Users/wanggaoshuai/Downloads/1-"+ IdUtil.fastSimpleUUID() + ".pdf";
        Excel2Pdf.excel2pdf(sourceFilePath,desFilePath);
    }
}
