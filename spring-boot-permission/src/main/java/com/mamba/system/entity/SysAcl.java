package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_acl")
public class SysAcl extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 权限码 */
    private String code;

    /** 权限名称 */
    private String name;

    /** 权限所在的权限模块id */
    @TableField("acl_module_id")
    private Integer aclModuleId;

    /** 请求的url */
    private String url;

    /** 类型，1：菜单，2：按钮，3：其他 */
    private Integer type;

    /** 状态，1：正常，0：冻结 */
    private Integer status;

    /** 权限在当前模块下的顺序 */
    private Integer seq;

    /** 备注 */
    private String remark;
}
