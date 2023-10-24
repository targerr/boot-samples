package com.example.service;

import com.example.dto.AclModuleLevelDto;
import com.example.vo.DeptVo;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/19 16:40
 * @Classname SysTreeService
 * @Description
 */
public interface SysTreeService {
    public List<DeptVo> getDeptTree();

    List<AclModuleLevelDto> aclModuleTree();

    List<AclModuleLevelDto> userAclTree(int userId);
}
