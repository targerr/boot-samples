package com.mamba.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysAclDTO;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.entity.SysAclModule;
import com.mamba.system.mapper.SysAclMapper;
import com.mamba.system.mapper.SysAclModuleMapper;
import com.mamba.system.service.SysAclService;
import com.mamba.system.service.SysRoleAclService;
import com.mamba.system.vo.SysAclVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysAclServiceImpl extends ServiceImpl<SysAclMapper, SysAcl> implements SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysRoleAclService sysRoleAclService;

    @Override
    public PageResult<SysAclVO> page(PageQueryDTO query) {
        LambdaQueryWrapper<SysAcl> wrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(query.getKeyword())
                .filter(StringUtils::hasText)
                .ifPresent(keyword -> wrapper.and(w -> w
                        .like(SysAcl::getName, keyword)
                        .or().like(SysAcl::getCode, keyword)
                ));
        Optional.ofNullable(query.getStatus())
                .ifPresent(status -> wrapper.eq(SysAcl::getStatus, status));

        wrapper.orderByAsc(SysAcl::getSeq);

        Page<SysAcl> page = sysAclMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), wrapper
        );

        List<Integer> moduleIds = page.getRecords().stream()
                .map(SysAcl::getAclModuleId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, String> moduleNameMap;
        if (!moduleIds.isEmpty()) {
            moduleNameMap = sysAclModuleMapper.selectBatchIds(moduleIds).stream()
                    .collect(Collectors.toMap(SysAclModule::getId, SysAclModule::getName, (a, b) -> a));
        } else {
            moduleNameMap = Collections.emptyMap();
        }

        List<SysAclVO> voList = page.getRecords().stream()
                .map(acl -> {
                    SysAclVO vo = convertToVO(acl);
                    vo.setAclModuleName(moduleNameMap.getOrDefault(acl.getAclModuleId(), ""));
                    return vo;
                }).collect(Collectors.toList());

        Page<SysAclVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return PageResult.of(voPage);
    }

    @Override
    public List<SysAclVO> listByModuleId(Integer moduleId) {
        return sysAclMapper.selectList(
                new LambdaQueryWrapper<SysAcl>()
                        .eq(SysAcl::getAclModuleId, moduleId)
                        .orderByAsc(SysAcl::getSeq)
        ).stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public void saveAcl(SysAclDTO dto) {
        SysAcl acl = new SysAcl();
        BeanUtils.copyProperties(dto, acl);
        acl.setStatus(1);
        sysAclMapper.insert(acl);
    }

    @Override
    public void updateAcl(SysAclDTO dto) {
        Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new BusinessException("权限ID不能为空"));

        SysAcl acl = sysAclMapper.selectById(dto.getId());
        Optional.ofNullable(acl)
                .orElseThrow(() -> new BusinessException("权限不存在"));

        BeanUtils.copyProperties(dto, acl);
        sysAclMapper.updateById(acl);
    }

    @Override
    public void changeStatus(Integer id, Integer status) {
        SysAcl acl = sysAclMapper.selectById(id);
        Optional.ofNullable(acl)
                .orElseThrow(() -> new BusinessException("权限不存在"));
        acl.setStatus(status);
        sysAclMapper.updateById(acl);
    }

    @Override
    public List<SysAclVO> listByRole(Integer roleId) {
        List<Integer> aclIds = sysRoleAclService.getAclIdsByRoleId(roleId);
        Set<Integer> checkedIds = aclIds.stream().collect(Collectors.toSet());

        List<SysAcl> allAcls = sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().orderByAsc(SysAcl::getSeq));

        List<Integer> moduleIds = allAcls.stream()
                .map(SysAcl::getAclModuleId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, String> moduleNameMap;
        if (!moduleIds.isEmpty()) {
            moduleNameMap = sysAclModuleMapper.selectBatchIds(moduleIds).stream()
                    .collect(Collectors.toMap(SysAclModule::getId, SysAclModule::getName, (a, b) -> a));
        } else {
            moduleNameMap = Collections.emptyMap();
        }

        return allAcls.stream()
                .map(acl -> {
                    SysAclVO vo = convertToVO(acl);
                    vo.setAclModuleName(moduleNameMap.getOrDefault(acl.getAclModuleId(), ""));
                    vo.setChecked(checkedIds.contains(acl.getId()));
                    return vo;
                }).collect(Collectors.toList());
    }

    private SysAclVO convertToVO(SysAcl acl) {
        SysAclVO vo = new SysAclVO();
        BeanUtils.copyProperties(acl, vo);
        vo.setTypeDesc(getTypeDesc(acl.getType()));
        vo.setStatusDesc(Integer.valueOf(1).equals(acl.getStatus()) ? "正常" : "冻结");
        return vo;
    }

    private String getTypeDesc(Integer type) {
        if (type == null) return "";

        switch (type) {
            case 1:
                return "菜单";
            case 2:
                return "按钮";
            case 3:
                return "其他";
            default:
                return "未知";
        }
    }
}
