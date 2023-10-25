package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(name = "部门model", description = "部门实体")
public class DeptReq {

    @Schema(description = "部门id")
    private Integer id;

    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不可以为空")
    @Length(max = 15, min = 2, message = "部门名称长度需要在2-15个字之间")
    private String name;

    @Schema(description = "父id")
    private Integer parentId = 0;

    @Schema(description = "顺序")
    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    @Schema(description = "备注")
    @Length(max = 150, message = "备注的长度需要在150个字以内")
    private String remark;
}
