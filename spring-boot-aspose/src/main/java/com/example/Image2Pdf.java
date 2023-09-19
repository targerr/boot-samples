package com.example;

import cn.hutool.core.util.IdUtil;
import com.aspose.cells.License;
import com.aspose.pdf.Document;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

/**
 * @Author: wgs
 * @Date 2023/8/30 16:21
 * @Classname Pdf2Picture
 * @Description
 */
public class Image2Pdf {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/wanggaoshuai/Downloads/1-a0a8833085d441a1898ddb5f18849b22.pdf";
        String desFilePath = "/Users/wanggaoshuai/Downloads/image";
        Image2Pdf.pdfToImage(sourceFilePath, desFilePath);
    }

    /**
     * pfd转图片
     *
     * @param pdf     源文件全路径
     * @param outPath 转后的文件夹路径
     */
    public static void pdfToImage(String pdf, String outPath) {
        if (!isWindows()) {
            Locale locale = new Locale("zh", "cn");
            Locale.setDefault(locale);

        }
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return;
        }

        try {
            long old = System.currentTimeMillis();
            System.out.println("convert pdf2jpg begin");
            Document pdfDocument = new Document(pdf);
            //图片宽度：800
            //图片高度：100
            // 分辨率 960
            //Quality [0-100] 最大100
            //例： new JpegDevice(800, 1000, resolution, 90);
            Resolution resolution = new Resolution(960);
            JpegDevice jpegDevice = new JpegDevice(resolution);
            for (int index = 1; index <= pdfDocument.getPages().size(); index++) {
                System.out.println("----开始第" + index + "张转图片----");
                // 输出路径
                File file = new File(outPath + "/" + index + ".jpg");
                FileOutputStream fileOs = new FileOutputStream(file);
                jpegDevice.process(pdfDocument.getPages().get_Item(index), fileOs);
                fileOs.close();
                System.out.println("----开始第" + index + "张转图片结束----");
            }

            long now = System.currentTimeMillis();
            System.out.println("convert pdf2jpg completed, elapsed ：" + ((now - old) / 1000.0) + "秒");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("convert pdf2jpg error:" + e);
        }


    }

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

        public static boolean isWindows() {
            String os = System.getProperty("os.name");

            if (os.toLowerCase().startsWith("win")) {
                return true;
            }
            return false;
        }

    public Integer getPdfPageSize(byte[] pdfByte) {
        MultipartFile multipartFile = fileByte2MultipartFile(pdfByte, IdUtil.getSnowflake() + ".pdf");
        try {
            Document pdfDocument = new Document(multipartFile.getInputStream());
            return pdfDocument.getPages().size();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    protected MultipartFile fileByte2MultipartFile(byte[] fileByte, String name) {
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(name, name, null, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return multipartFile;
    }


}
