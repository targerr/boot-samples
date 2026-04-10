package com.mamba.system.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoleUserDTO {

    @NotNull
    private Integer roleId;

    @NotEmpty
    private List<Integer> userIds;
}
