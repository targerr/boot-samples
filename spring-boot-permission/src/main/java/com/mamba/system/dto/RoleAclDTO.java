package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleAclDTO {

    @NotNull
    private Integer roleId;

    @NotEmpty
    private List<Integer> aclIds;
}
