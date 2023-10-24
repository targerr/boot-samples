package com.example.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.entity.SysAclModule;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/23
 * @Classname AclModelTreeVo
 * @since 1.0.0
 */
@Tag(name = "权限模块树")
@Data
public class AclModuleLevelDto {
    @Schema(description = "权限模块id")
    private Integer id;

    @Schema(description = "权限模块名称")
    private String name;

    @Schema(description = "权限模块父id")
    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不可以为空")
    private Integer seq;

    @Schema(description = "权限模块状态")
    private Integer status;

    @Schema(description = "权限模块备注")
    @Length(max = 200, message = "权限模块备注需要在200个字之间")
    private String remark;

    List<AclModuleLevelDto> children;

    List<AclDto> aclList = Lists.newArrayList();

    public static List<AclModuleLevelDto> aclModuleConvertTreeVo(List<SysAclModule> sysAclModules) {
        List<AclModuleLevelDto> aclModuleLevelDtos = new ArrayList<>();
        if (CollectionUtils.isEmpty(sysAclModules)) {
            return aclModuleLevelDtos;
        }

        sysAclModules.forEach(e -> {
            AclModuleLevelDto vo = new AclModuleLevelDto();
            BeanUtils.copyProperties(e, vo);
            aclModuleLevelDtos.add(vo);
        });
        return aclModuleLevelDtos;
    }
}
