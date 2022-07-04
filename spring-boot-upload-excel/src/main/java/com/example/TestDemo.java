package com.example;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试单表对象 test_demo
 */
@Data
public class TestDemo  {

    private static final long serialVersionUID = 1L;


    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();

    /**
     * 主键
     */
    private Long id;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * key键
     */
    private String testKey;

    /**
     * 值
     */
    private String value;

    /**
     * 版本
     */
    private Long version;

    /**
     * 删除标志
     */
    private Long delFlag;

}
