package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.dto.AclDto;
import com.example.dto.AclModuleLevelDto;
import com.example.entity.SysAcl;
import com.example.entity.SysAclModule;
import com.example.entity.SysDept;
import com.example.mapper.SysAclMapper;
import com.example.mapper.SysAclModuleMapper;
import com.example.mapper.SysDeptMapper;
import com.example.service.SysCoreService;
import com.example.service.SysTreeService;
import com.example.vo.DeptVo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2023/10/19 16:41
 * @Classname SysTreeServiceImpl
 * @Description
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysCoreService sysCoreService;
    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public List<DeptVo> getDeptTree() {
        List<SysDept> sysDeptList = sysDeptMapper.selectList(null);
        if (CollectionUtils.isEmpty(sysDeptList)) {
            return new ArrayList<DeptVo>();
        }

        final List<DeptVo> deptVos = DeptVo.addDeptList(sysDeptList);

        return buildDeptTree(deptVos, 0);
    }

    @Override
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> sysAclModules = sysAclModuleMapper.selectList(null);
        if (CollectionUtils.isEmpty(sysAclModules)) {
            return new ArrayList<>();
        }

        List<AclModuleLevelDto> aclModuleLevelDtos = AclModuleLevelDto.aclModuleConvertTreeVo(sysAclModules);
        return buildAclTree(aclModuleLevelDtos, 0);
    }

    @Override
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = userAclList.stream().map(e -> {
            AclDto aclDto = new AclDto();
            BeanUtils.copyProperties(e, aclDto);
            aclDto.setHasAcl(true);
            aclDto.setChecked(true);
            return aclDto;
        }).collect(Collectors.toList());

        return aclListToTree(aclDtoList);
    }

    @Override
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1、当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2、当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3、当前系统所有权限点
        List<AclDto> aclDtoList = Lists.newArrayList();
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclMapper.selectList(null);
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // 获取权限树
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        final Map<Integer, List<AclDto>> moduleIdAclMap = aclDtoList.stream().filter(e -> e.getStatus().equals(1)).collect(Collectors.groupingBy(AclDto::getAclModuleId));

        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Map<Integer, List<AclDto>> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }


        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getChildren(), moduleIdAclMap);
        }
    }

    private List<AclModuleLevelDto> buildAclTree(List<AclModuleLevelDto> aclModuleLevelDtos, int parentId) {
        List<AclModuleLevelDto> rootTrees = aclModuleLevelDtos.stream()
                .filter(e -> e.getParentId().equals(parentId))
                .peek(m -> m.setChildren(getAclChildren(m, aclModuleLevelDtos)))
                .collect(Collectors.toList());

        return rootTrees;
    }

    private List<AclModuleLevelDto> getAclChildren(AclModuleLevelDto aclModule, List<AclModuleLevelDto> aclModuleLevelDtos) {
        return aclModuleLevelDtos.stream()
                .filter(e -> aclModule.getId().equals(e.getParentId()))
                .peek(m -> getAclChildren(m, aclModuleLevelDtos))
                .collect(Collectors.toList());
    }

    private List<DeptVo> buildDeptTree(List<DeptVo> deptVoList, Integer parentId) {
        return deptVoList.stream()
                .filter(e -> e.getParentId().equals(parentId))
                .peek(m -> m.setChildren(getChildren(m, deptVoList)))
                .collect(Collectors.toList());

    }

    private List<DeptVo> getChildren(DeptVo deptVo, List<DeptVo> deptVoList) {
        return deptVoList.stream()
                .filter(e -> deptVo.getId().equals(e.getParentId()))
                .peek(m -> m.setChildren(getChildren(m, deptVoList)))
                .collect(Collectors.toList());

    }
}
