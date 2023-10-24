package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/20
 * @Classname RoleUserReq
 * @since 1.0.0
 */
@Schema(name = "用户与权限点", description = "用户与权限点")
@Data
public class RoleAclReq {
    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
    @Schema(description = "权限点id集合")
    @NotNull(message = "权限点id集合不能为空")
    private List<Integer> aclIds;
}
