package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysUserDTO {

    /** 更新时传，新增时不传 */
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String telephone;

    private String mail;

    /** 新增时必填，更新时不填表示不修改 */
    private String password;

    private Integer deptId;

    private String remark;
}
