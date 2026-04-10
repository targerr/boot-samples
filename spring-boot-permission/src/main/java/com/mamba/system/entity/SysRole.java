package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 角色名称 */
    private String name;

    /** 角色类型，1：管理员角色，2：其他 */
    private Integer type;

    /** 状态，1：可用，0：冻结 */
    private Integer status;

    /** 备注 */
    private String remark;
}
