package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.lang.model.element.Name;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/20
 * @Classname RoleUserReq
 * @since 1.0.0
 */
@Schema(name = "用户角色", description = "用户角色实体")
@Data
public class RoleUserReq {
    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
    @Schema(description = "用户id集合")
    @NotNull(message = "用户id集合不能为空")
    private List<Integer> userIds;
}
