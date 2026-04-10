package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.system.dto.RoleAclDTO;
import com.mamba.system.entity.SysRoleAcl;

import java.util.List;

public interface SysRoleAclService extends IService<SysRoleAcl> {

    List<Integer> getAclIdsByRoleId(Integer roleId);

    void assignAcls(RoleAclDTO dto);
}
