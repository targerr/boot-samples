package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysRoleDTO;
import com.mamba.system.entity.SysRole;
import com.mamba.system.vo.SysRoleVO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRoleVO> page(PageQueryDTO query);

    void saveRole(SysRoleDTO dto);

    void updateRole(SysRoleDTO dto);

    void changeStatus(Integer id, Integer status);

    List<SysRoleVO> listAll();
}
