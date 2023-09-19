package com.example;

import cn.hutool.core.util.IdUtil;
import com.aspose.pdf.*;

import java.util.Locale;

/**
 * @Author: wgs
 * @Date 2023/8/30 15:52
 * @Classname Excel2Pdf
 * @Description
 */
public class Pdf2Pdf {

    /**
     * @param sourceFilePath 需要被转换的pdf全路径带文件名
     * @param desFilePath    转换之后pdf的全路径带文件名
     */
    public static void pdf2pdf(String sourceFilePath, String desFilePath) {
        if (!isWindows()) {
            Locale locale = new Locale("zh", "cn");
            Locale.setDefault(locale);

        }
        try {
            long old = System.currentTimeMillis();

            // Load the PDF file
            Document document = new Document(sourceFilePath);

            // get particular page
            Page pdfPage = document.getPages().get_Item(1);


            String txt = "此文档主要为解决java项目中生成pdf、word文件";
            // create text fragment
            TextFragment textFragment = new TextFragment(txt);


            textFragment.setPosition(new Position(130, 700));

            // set text properties
            textFragment.getTextState().setFont(FontRepository.findFont("Verdana"));
            textFragment.getTextState().setFontSize(14);
//        textFragment.getTextState().setForegroundColor(Color.getBlue());
//        textFragment.getTextState().setBackgroundColor(Color.getLightGray());

            // create TextBuilder object
            TextBuilder textBuilder = new TextBuilder(pdfPage);
            // append the text fragment to the PDF page
            textBuilder.appendText(textFragment);

            // extracted(pdfPage);
            long now = System.currentTimeMillis();


            // Save resulting PDF document.
            document.save(desFilePath);

            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");

        if (os.toLowerCase().startsWith("win")) {
            return true;
        }
        return false;
    }



    /**
     * 根据pdf的坐标位置进行签字
     *
     * @param pdfFilePath pdf文件路径
     * @param pageNum     在第几页进行签字
     * @param value       签字内容
     * @param font        字体不能为中文，比如宋体为 SimSun，黑体为 SimHei 具体可以查看 C:\Windows\Fonts 下字体文件的真实名称
     * @param fontSize    字体大小
     * @param xIndent     x坐标
     * @param yIndent     y坐标
     * @author anshuo
     */
    public static void pdfSign(String pdfFilePath, int pageNum, String value, String font, String fontSize, double xIndent, double yIndent) {
        if (!isWindows()) {
            Locale locale = new Locale("zh", "cn");
            Locale.setDefault(locale);

        }

        Document document = new Document(pdfFilePath);
        if (document != null) {
            Page page = document.getPages().get_Item(pageNum);
            if (page != null) {
                TextParagraph paragraph = new TextParagraph();
                paragraph.getFormattingOptions().setWrapMode(
                        TextFormattingOptions.WordWrapMode.ByWords);
                TextState textState = new TextState();

                float pondSize = getFontPound(fontSize);

                Font pdfFont = FontRepository.findFont(font, true);
                textState.setFont(pdfFont);
                textState.setFontSize(pondSize);


                paragraph.appendLine(value, textState);
                Position position = new Position(xIndent, yIndent);
                paragraph.setPosition(position);
                TextBuilder textBuilder = new TextBuilder(page);
                textBuilder.appendParagraph(paragraph);
            } else {
                System.out.println("页面第：" + pageNum + "页不存在");
            }

            String path = "/Users/wanggaoshuai/Downloads/1-" + IdUtil.fastSimpleUUID() + ".pdf";

            System.out.println(path);
            document.save(path);
        }
    }


    //根据输入字体返回字体大小
    public static float getFontPound(String font) {
        float size = 3.70F;
        if (font.endsWith("初号")) {
            return 42;
        } else if (font.endsWith("小初")) {
            return 36;
        } else if (font.endsWith("一号")) {
            return 26;
        } else if (font.endsWith("小一")) {
            return 24;
        } else if (font.endsWith("二号")) {
            return 22;
        } else if (font.endsWith("小二")) {
            return 18;
        } else if (font.endsWith("三号")) {
            return 16;
        } else if (font.endsWith("小三")) {
            return 15;
        } else if (font.endsWith("四号")) {
            return 14;
        } else if (font.endsWith("小四")) {
            return 12;
        } else if (font.endsWith("五号")) {
            return 10.5F;
        } else if (font.endsWith("小五")) {
            return 9;
        } else if (font.endsWith("六号")) {
            return 7.5F;
        } else if (font.endsWith("小六")) {
            return 6.5F;
        } else if (font.endsWith("七号")) {
            return 5.5F;
        } else if (font.endsWith("八号")) {
            return 5;
        }
        return size;
    }

    public static void main(String[] args) {
        String sourceFilePath = "/Users/wanggaoshuai/Downloads/8650207837.pdf";
        String desFilePath = "/Users/wanggaoshuai/Downloads/1-" + IdUtil.fastSimpleUUID() + ".pdf";
        Pdf2Pdf.pdf2pdf(sourceFilePath, desFilePath);
    }
}
