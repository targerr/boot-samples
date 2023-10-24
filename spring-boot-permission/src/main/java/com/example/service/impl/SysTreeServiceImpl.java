package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.dto.AclDto;
import com.example.dto.AclModuleLevelDto;
import com.example.entity.SysAcl;
import com.example.entity.SysAclModule;
import com.example.entity.SysDept;
import com.example.mapper.SysAclModuleMapper;
import com.example.mapper.SysDeptMapper;
import com.example.service.SysCoreService;
import com.example.service.SysTreeService;
import com.example.vo.DeptVo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
