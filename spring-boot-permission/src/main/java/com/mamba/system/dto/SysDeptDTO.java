package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysDeptDTO {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    private String name;

    /**
     * 默认0表示顶级部门
     */
    private Integer parentId;

    private Integer seq;

    private String remark;
}
