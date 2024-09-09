package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.binding.EchoField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 市表
 *
 * @TableName city
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "city")
public class City implements Serializable {
    /**
     * 序列
     */
    @TableId
    private String id;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createDateTime;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 修改时间
     */
    private Date modifyDateTime;

    /**
     * 修改人
     */
    private String modifyName;

    /**
     * 删除状态（0：未删除，1：删除）
     */
    private Boolean isDelete;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    @EchoField(ref = "stateName", dictType = "state", api = "cityTypeService")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    /**
     * 标签
     */
    private String label;


    /**
     * 排序
     */
    private Integer sorting;

    /**
     * 版本号
     */
    @EchoField(ref = "versionName", dictType = "version", api = "versionService")
    private Integer version;

    @TableField(exist = false)
    private String versionName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}