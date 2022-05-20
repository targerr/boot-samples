### 整合pdf

##### 示例

第一步：创建starter工程spring-boot-file-pdf

```xml

<dependencies>
    <!-- 工具类大全 -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
    <!-- pdf操作 -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itextpdf</artifactId>
        <version>5.5.13</version>
    </dependency>
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itext-asian</artifactId>
        <version>5.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>io</artifactId>
        <version>7.1.8</version>
    </dependency>
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>kernel</artifactId>
        <version>7.1.8</version>
    </dependency>
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.20</version>
    </dependency>
    <dependency>
        <groupId>com.lowagie</groupId>
        <artifactId>itext</artifactId>
        <version>2.1.7</version>
    </dependency>
</dependencies>
```

第二步：工具类

~~~java
package com.example.utils;

import com.itextpdf.io.codec.Base64;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/5/17 11:14
 * @Classname PdfUtils
 * @Description
 */
public class PdfUtils {
    /**
     * @param template 模版的二进制刘
     * @param address  输出的文件地址
     * @param data     数据集合
     * @param picture  图片集合
     * @return: void
     * @date: 2021/5/21 10:17 上午
     */
    public static void generate(byte[] template, String address, Map<String, String> data, Map<String, String> picture) throws Exception {
        //设置编码
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        PdfReader pdfReader = new PdfReader(template);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(address));
        AcroFields form = pdfStamper.getAcroFields();
        form.addSubstitutionFont(baseFont);
        /**
         * 设置pdf内容
         */
        for (String key : data.keySet()) {
            String value = data.get(key);
            //key对应模板数据域的名称
            form.setField(key, value);
        }
        /**
         * 放入图片
         */
        if (picture != null && picture.size() > 0) {
            for (String key : picture.keySet()) {
                String value = picture.get(key);
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                Image image = Image.getInstance(Base64.decode(value));
                PdfContentByte under = pdfStamper.getOverContent(pageNo);
                //设置图片大小
                image.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
                //设置图片位置
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
        }


        //设置不可编辑
        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
    }


    /**
     * @param template 模版的二进制刘
     * @param address  输出的文件地址
     * @param data     数据集合
     * @param picture  图片
     * @auther: 孙凯伦
     * @mobile: 13777579028
     * @email: 376253703@qq.com
     * @name: generateByte
     * @description: TODO       pdf模版生成
     * @return: void
     * @date: 2021/5/21 10:19 上午
     */
    public static void generateByte(byte[] template, String address, Map<String, String> data, Map<String, byte[]> picture) throws Exception {
        //设置编码
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        PdfReader pdfReader = new PdfReader(template);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(address));
        AcroFields form = pdfStamper.getAcroFields();
        form.addSubstitutionFont(baseFont);

        /**
         * 设置pdf内容
         */
        for (String key : data.keySet()) {
            String value = data.get(key);
            //key对应模板数据域的名称
            form.setField(key, value);
        }
        /**
         * 放入图片
         */
        for (String key : picture.keySet()) {
            byte[] value = picture.get(key);
            int pageNo = form.getFieldPositions(key).get(0).page;
            Rectangle signRect = form.getFieldPositions(key).get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            Image image = Image.getInstance(value);
            PdfContentByte under = pdfStamper.getOverContent(pageNo);
            //设置图片大小
            image.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
            //设置图片位置
            image.setAbsolutePosition(x, y);
            under.addImage(image);
        }


        //设置不可编辑
        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
    }

    /**
     * pdf转换png
     *
     * @param png  图片存放地址
     * @param pdf  PDF二进制流
     * @param name 名称
     */
    public static void png(String png, byte[] pdf, String name) {
        try {
            PDDocument doc = PDDocument.load(pdf);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // Windows native DPI
                BufferedImage image = renderer.renderImageWithDPI(i, 144);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, "png", new File(png + name + "_" + (i + 1) + ".png"));
                //关闭
                image.flush();
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * pdf转换png
     *
     * @param png  图片存放地址
     * @param pdf  PDF地址
     * @param name 名称
     */
    public static void png(String png, String pdf, String name) {
        File file = new File(pdf);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // Windows native DPI
                BufferedImage image = renderer.renderImageWithDPI(i, 200);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, "png", new File(png + name + "_" + (i + 1) + ".png"));
                //关闭
                image.flush();
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


~~~

第三步：测试
```java
package com.example;

import cn.hutool.core.io.FileUtil;
import com.example.utils.PdfUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/5/20 11:21
 * @Classname PdfTest
 * @Description
 */
public class PdfTest {

    public static void main(String[] args) throws Exception {
        byte[] bytes = FileUtil.readBytes("/Users/Downloads/1.pdf");
        Map<String, String> map = new HashMap<>();
        map.put("Name", "tom");
        map.put("Age", "23");
        String address = "/Users/Downloads/2.pdf";
        PdfUtils.generate(bytes, address, map, null);
        System.out.println("-------");
//        png("/Users/Downloads/", "/Users//Downloads/2.pdf", "192847040_0brokerQR");
//        pdf2Pic("/Users/Downloads/", "/Users/Downloads/22f3bB1E6af.pdf", "22f3bBE6af");
    }

}

```
