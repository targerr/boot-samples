package com.mamba.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysRoleDTO;
import com.mamba.system.entity.SysRole;
import com.mamba.system.mapper.SysRoleMapper;
import com.mamba.system.service.SysRoleService;
import com.mamba.system.vo.SysRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageResult<SysRoleVO> page(PageQueryDTO query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(query.getKeyword())
                .filter(StringUtils::hasText)
                .ifPresent(keyword -> wrapper.like(SysRole::getName, keyword));
        Optional.ofNullable(query.getStatus())
                .ifPresent(status -> wrapper.eq(SysRole::getStatus, status));

        Page<SysRole> page = sysRoleMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), wrapper
        );

        List<SysRoleVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<SysRoleVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return PageResult.of(voPage);
    }

    @Override
    public void saveRole(SysRoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        role.setStatus(1);
        sysRoleMapper.insert(role);
    }

    @Override
    public void updateRole(SysRoleDTO dto) {
        Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new BusinessException("角色ID不能为空"));

        SysRole role = sysRoleMapper.selectById(dto.getId());
        Optional.ofNullable(role)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        BeanUtils.copyProperties(dto, role);
        sysRoleMapper.updateById(role);
    }

    @Override
    public void changeStatus(Integer id, Integer status) {
        SysRole role = sysRoleMapper.selectById(id);
        Optional.ofNullable(role)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        role.setStatus(status);
        sysRoleMapper.updateById(role);
    }

    @Override
    public List<SysRoleVO> listAll() {
        return sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1)
        ).stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private SysRoleVO convertToVO(SysRole role) {
        SysRoleVO vo = new SysRoleVO();
        BeanUtils.copyProperties(role, vo);
        vo.setTypeDesc(Integer.valueOf(1).equals(role.getType()) ? "管理员角色" : "其他");
        vo.setStatusDesc(Integer.valueOf(1).equals(role.getStatus()) ? "可用" : "冻结");
        return vo;
    }
}
