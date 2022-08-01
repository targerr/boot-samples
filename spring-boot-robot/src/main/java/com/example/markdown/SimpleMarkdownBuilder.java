package com.example.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/29 14:43
 * @Classname SimpleMarkdownBuilder
 * @Description
 */
public class SimpleMarkdownBuilder {
    private StringBuilder stringBuilder = new StringBuilder();

    public static SimpleMarkdownBuilder create() {
        SimpleMarkdownBuilder markdownBuilder = new SimpleMarkdownBuilder();
        return markdownBuilder;
    }

    /**
     * 使用#标记，标题
     * @param content
     * @param level
     * @return
     */
    public SimpleMarkdownBuilder title(String content, int level) {
        if (level > 0 && level < 6) {
            for (int i = 0; i < level; i++) {
                stringBuilder.append("#");
            }
            stringBuilder.append(" ").append(content).append("\n\n");
        }
        return this;
    }

    /**
     * 构建内容
     * @param content 内容
     * @param lineFeed 是否换行
     * @return
     */
    public SimpleMarkdownBuilder text(String content, boolean lineFeed) {
        return colorText(content, null, lineFeed);
    }

    /**
     * 构建颜色内容
     * @param content 内容
     * @param color RGB码
     * @param lineFeed 是否换行
     * @return
     */
    public SimpleMarkdownBuilder colorText(String content, String color, boolean lineFeed) {
        if (StrUtil.isNotEmpty(color)) {
            stringBuilder.append("<font color=").append(color).append(">").append(content).append("</font>");
            if (lineFeed)
                stringBuilder.append("\n\n");

            return this;
        }
        stringBuilder.append(content);
        if (lineFeed)
            stringBuilder.append("\n\n");
        return this;
    }

    /**
     * 无序列表+冒号
     * @param content
     * @return
     */
    public static String pointText(String content) {
        return "- " + content + ": ";
    }

    /**
     * 无序列表
     * @param contentList
     * @return
     */
    public SimpleMarkdownBuilder point(List<?> contentList) {
        if (contentList != null && contentList.size() > 0) {
            contentList.forEach(x -> stringBuilder.append("- ").append(x).append("\n"));
            stringBuilder.append("\n");
        }
        return this;
    }

    public SimpleMarkdownBuilder point(Object... contentList) {
        if (contentList != null && contentList.length > 0) {
            Arrays.stream(contentList).forEach(x -> stringBuilder.append("- ").append(x).append("\n"));
            stringBuilder.append("\n");
        }
        return this;
    }

    /**
     * 有序列表
     * @param list
     * @return
     */
    public SimpleMarkdownBuilder orderPoint(List<?> list) {
        for (int i = 0; i < list.size(); i++)
            stringBuilder.append(i + 1).append(". ").append(list.get(i)).append("\n");
        stringBuilder.append("\n");
        return this;
    }

    public SimpleMarkdownBuilder orderPoint(Object... list) {
        for (int i = 0; i < list.length; i++)
            stringBuilder.append(i + 1).append(". ").append(list[i]).append("\n");
        stringBuilder.append("\n");
        return this;
    }

    /**
     *
     * @param content
     * @param level
     * @return
     */
    public SimpleMarkdownBuilder code(String content, int level) {
        if (level > 0 && level < 4) {
            String str = "`````````".substring(0, level);
            if (level != 3)
                stringBuilder.append(String.format("%s%s%s", str, content, str));
            else
                stringBuilder.append(String.format("%s\n%s\n%s\n", str, content, str));
        }
        return this;
    }

    /**
     * 拼接链接
     * @param explain
     * @param url
     * @return
     */
    public SimpleMarkdownBuilder linked(String explain, String url) {
        stringBuilder.append("![").append(explain).append("](").append(url).append(")");
        return this;
    }

    /**
     * 表格
     * @param map
     * @param keyName
     * @param valueName
     * @param alignment
     * @return
     */
    public SimpleMarkdownBuilder keyValue(Map<?, ?> map, String keyName, String valueName, String alignment) {
        if (CollUtil.isNotEmpty(map)) {
            stringBuilder.append("|").append(keyName).append("|").append(valueName).append("|").append("\n");
            String value = "-:";
            stringBuilder.append(value).append("|").append(value).append("|").append("\n");
            map.forEach((x, y) -> stringBuilder.append("|").append(x).append("|").append(y).append("|").append("\n"));
            stringBuilder.append("\n\n");
        }
        return this;
    }

    /**
     * 分割线
     * @return
     */
    public SimpleMarkdownBuilder partingLine() {
        stringBuilder.append("---");
        stringBuilder.append("\n\n");
        return this;
    }

    /**
     * 换行
     * @return
     */
    public SimpleMarkdownBuilder nextLine() {
        stringBuilder.append("\n");
        return this;
    }

    /**
     * 粗体文本
     * @param text
     * @return
     */
    public static String bold(String text) {
        return String.format("**%s**", text);
    }

    /**
     * 构建字符串
     * @return
     */
    public String build() {
        return stringBuilder.toString();
    }

    /**
     * 示例
     * @param title
     * @param msg
     */
    public static void buildMsg(String title, String msg) {
        SimpleMarkdownBuilder builder = SimpleMarkdownBuilder.create()
                .colorText(title, "#FF0000", false);

        String messge = builder.build();
    }
}
