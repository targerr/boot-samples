package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mamba.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户名称 */
    private String username;

    /** 手机号 */
    private String telephone;

    /** 邮箱 */
    private String mail;

    /** 加密后的密码 */
    private String password;

    /** 用户所在部门id */
    @TableField("dept_id")
    private Integer deptId;

    /** 状态，1：正常，0：冻结，2：删除 */
    private Integer status;

    /** 备注 */
    private String remark;
}
