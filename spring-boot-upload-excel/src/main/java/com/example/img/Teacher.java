package com.example.img;

/**
 * @Author: wgs
 * @Date 2023/12/20
 * @Classname Teacher
 * @since 1.0.0
 */

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.net.URL;

/**
 * @Description 老师信息实体类
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ContentRowHeight(120)
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {

    /**
     * 教师编号
     */
    @ExcelProperty("教师编号")
    @ExcelIgnore
    private Integer teacherId;
    /**
     * 教师名称
     */
    @ExcelProperty("教师名称")
    private String teacherName;
    /**
     * 教师图片
     */
    @ExcelProperty(value = {"教师图片"}, converter = SxjgUrlImageConverter.class)
    private URL teacherImage;
    /**
     * 教师状态: 0 -任教中 1 - 为任教
     */
    @ExcelProperty("教师状态")
    @ExcelIgnore
    private Integer teacherStatus;

    /**
     * 教师状态: 0 -任教中 1 - 为任教
     */
    @ExcelProperty("教师状态")
    private String teacherStatusStr;
    /**
     * 住址信息
     */
    @ExcelProperty("住址信息")
    private String address;
}