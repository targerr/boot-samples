package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysRoleDTO {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    private Integer type;

    private String remark;
}
