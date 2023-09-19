package com.example;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.cells.License;
import com.aspose.pdf.Document;
import com.aspose.pdf.Image;
import com.aspose.pdf.Page;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;
/**
 * @Author: wgs
 * @Date 2023/8/30 16:21
 * @Classname Pdf2Picture
 * @Description
 */
public class Pdf2Picture {
    protected void image2pdf(MultipartFile multipartFile) {

    }

    public static void main(String[] args) throws Exception {
        String source = "/Users/wanggaoshuai/Downloads/uml-v1.png";
        String sourceFilePath = "/Users/wanggaoshuai/Downloads/12323.pdf";
        image2pdf(source, sourceFilePath, "png");

    }

    /**
     * 获取license 去除水印
     *
     * @return
     */
    public static boolean getLicense() {
        try {
//            String license = "PExpY2Vuc2U+DQogIDxEYXRhPg0KICAgIDxQcm9kdWN0cz4NCiAgICAgIDxQcm9kdWN0PkFzcG9zZS5Ub3RhbCBmb3IgSmF2YTwvUHJvZHVjdD4NCiAgICAgIDxQcm9kdWN0PkFzcG9zZS5Xb3JkcyBmb3IgSmF2YTwvUHJvZHVjdD4NCiAgICA8L1Byb2R1Y3RzPg0KICAgIDxFZGl0aW9uVHlwZT5FbnRlcnByaXNlPC9FZGl0aW9uVHlwZT4NCiAgICA8U3Vic2NyaXB0aW9uRXhwaXJ5PjIwOTkxMjMxPC9TdWJzY3JpcHRpb25FeHBpcnk+DQogICAgPExpY2Vuc2VFeHBpcnk+MjA5OTEyMzE8L0xpY2Vuc2VFeHBpcnk+DQogICAgPFNlcmlhbE51bWJlcj44YmZlMTk4Yy03ZjBjLTRlZjgtOGZmMC1hY2MzMjM3YmYwZDc8L1NlcmlhbE51bWJlcj4NCiAgPC9EYXRhPg0KICA8U2lnbmF0dXJlPnNOTExLR01VZEYwcjhPMWtLaWxXQUdkZ2ZzMkJ2SmIvMlhwOHA1aXVEVmZaWG1ocHBvK2QwUmFuMVA5VEtkalY0QUJ3QWdLWHhKM2pjUVRxRS8ySVJmcXduUGY4aXROOGFGWmxWM1RKUFllRDN5V0U3SVQ1NUd6NkVpalVwQzdhS2Vvb2hUYjR3MmZwb3g1OHdXb0YzU05wNnNLNmpEZmlBVUdFSFlKOXBqVT08L1NpZ25hdHVyZT4NCjwvTGljZW5zZT4=";
//            InputStream is = BaseToInputStream(license);

            InputStream is = Excel2Pdf.class.getClassLoader().getResourceAsStream("license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * base64转inputStream
     *
     * @param base64string
     * @return
     */
    private static InputStream BaseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] bytes1 = decoder.decodeBuffer(base64string);
            byte[] bytes = StrUtil.utf8Bytes(base64string);
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    /**
     * 图片转PDF
     *
     * @param sourcePath
     * @param targetPath
     * @param imgType
     * @throws IOException
     */
    public static void image2pdf(String sourcePath, String targetPath, String imgType) throws IOException {
        if (!isWindows()) {
            Locale locale = new Locale("zh", "cn");
            Locale.setDefault(locale);

        }
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        long old = System.currentTimeMillis();
        //创建文档
        Document doc = new Document();
        //新增一页
        Page page = doc.getPages().add();
        //设置页边距
        page.getPageInfo().getMargin().setBottom(0);
        page.getPageInfo().getMargin().setTop(0);
        page.getPageInfo().getMargin().setLeft(0);
        page.getPageInfo().getMargin().setRight(0);
        //创建图片对象
        Image image = new Image();
        BufferedImage bufferedImage = ImageIO.read(new File(sourcePath));
        //获取图片尺寸
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imgType, baos);
        baos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        image.setImageStream(bais);
        //设置pdf页的尺寸与图片一样
        page.getPageInfo().setHeight(height);
        page.getPageInfo().setWidth(width);
        //添加图片
        page.getParagraphs().add(image);
        //保存
        doc.save(targetPath, SaveFormat.Pdf);

        long now = System.currentTimeMillis();
        System.out.println("convert pdf2jpg completed, elapsed ：" + ((now - old) / 1000.0) + "秒");
    }
    public static boolean isWindows() {
        String os = System.getProperty("os.name");

        if (os.toLowerCase().startsWith("win")) {
            return true;
        }
        return false;
    }
}
