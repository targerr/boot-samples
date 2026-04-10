package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.system.dto.RoleUserDTO;
import com.mamba.system.entity.SysRoleUser;

import java.util.List;

public interface SysRoleUserService extends IService<SysRoleUser> {

    List<Integer> getUserIdsByRoleId(Integer roleId);

    List<Integer> getRoleIdsByUserId(Integer userId);

    void assignUsers(RoleUserDTO dto);
}
