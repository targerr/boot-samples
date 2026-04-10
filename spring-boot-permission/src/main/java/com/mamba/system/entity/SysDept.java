package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 部门名称 */
    private String name;

    /** 上级部门id */
    @TableField("parent_id")
    private Integer parentId;

    /** 部门层级 */
    private String level;

    /** 部门在当前层级下的顺序 */
    private Integer seq;

    /** 备注 */
    private String remark;
}
