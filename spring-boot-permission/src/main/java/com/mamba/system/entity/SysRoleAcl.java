package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色-权限关联实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_acl")
public class SysRoleAcl extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 角色id */
    @TableField("role_id")
    private Integer roleId;

    /** 权限id */
    @TableField("acl_id")
    private Integer aclId;
}
