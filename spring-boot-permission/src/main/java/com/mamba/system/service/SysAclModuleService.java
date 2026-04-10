package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.system.dto.SysAclModuleDTO;
import com.mamba.system.entity.SysAclModule;
import com.mamba.system.vo.SysAclModuleVO;

import java.util.List;

public interface SysAclModuleService extends IService<SysAclModule> {

    List<SysAclModuleVO> treeWithAcls();

    void saveModule(SysAclModuleDTO dto);

    void updateModule(SysAclModuleDTO dto);

    void changeStatus(Integer id, Integer status);

    void deleteModule(Integer id);
}
