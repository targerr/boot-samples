package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限模块实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_acl_module")
public class SysAclModule extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 权限模块名称 */
    private String name;

    /** 上级权限模块id */
    @TableField("parent_id")
    private Integer parentId;

    /** 权限模块层级 */
    private String level;

    /** 权限模块在当前层级下的顺序 */
    private Integer seq;

    /** 状态，1：正常，0：冻结 */
    private Integer status;

    /** 备注 */
    private String remark;
}
