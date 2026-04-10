package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysUserDTO;
import com.mamba.system.entity.SysUser;
import com.mamba.system.vo.SysUserVO;

import java.util.*;

public interface SysUserService extends IService<SysUser> {

    SysUserVO login(String username, String password);

    SysUserVO getUserInfo();

    Map<String, Object> getUserPermissions();

    PageResult<SysUserVO> page(PageQueryDTO query);

    void saveUser(SysUserDTO dto);

    void updateUser(SysUserDTO dto);

    void changeStatus(Integer userId, Integer status);

    void deleteUsers(List<Integer> ids);

    List<SysUserVO> listByRoleId(Integer roleId);

    List<SysUserVO> listByDeptId(Integer deptId);
}
