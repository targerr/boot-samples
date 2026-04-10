package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysAclDTO;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.vo.SysAclVO;

import java.util.List;

public interface SysAclService extends IService<SysAcl> {

    PageResult<SysAclVO> page(PageQueryDTO query);

    List<SysAclVO> listByModuleId(Integer moduleId);

    void saveAcl(SysAclDTO dto);

    void updateAcl(SysAclDTO dto);

    void changeStatus(Integer id, Integer status);

    List<SysAclVO> listByRole(Integer roleId);
}
