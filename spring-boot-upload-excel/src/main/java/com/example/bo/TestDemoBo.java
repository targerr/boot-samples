package com.example.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试单表业务对象 test_demo
 *
 * @author Lion Li
 * @date 2021-07-26
 */

@Data
public class TestDemoBo  {

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
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空")
    private Long deptId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空")
    private Integer orderNum;

    /**
     * key键
     */
    @NotBlank(message = "key键不能为空")
    private String testKey;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空")
    private String value;

}
