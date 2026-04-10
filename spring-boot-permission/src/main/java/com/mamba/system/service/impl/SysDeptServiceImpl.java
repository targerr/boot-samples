package com.mamba.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.util.TreeUtil;
import com.mamba.system.dto.SysDeptDTO;
import com.mamba.system.entity.SysDept;
import com.mamba.system.entity.SysUser;
import com.mamba.system.mapper.SysDeptMapper;
import com.mamba.system.mapper.SysUserMapper;
import com.mamba.system.service.SysDeptService;
import com.mamba.system.vo.SysDeptVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysDeptVO> tree() {
        List<SysDeptVO> voList = sysDeptMapper.selectList(
                new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSeq)
        ).stream()
                .map(dept -> {
                    SysDeptVO vo = new SysDeptVO();
                    BeanUtils.copyProperties(dept, vo);
                    return vo;
                }).collect(Collectors.toList());
        return TreeUtil.buildTreeSorted(voList, SysDeptVO::getId, SysDeptVO::getParentId, SysDeptVO::setChildren, SysDeptVO::getSeq);
    }

    @Override
    public void saveDept(SysDeptDTO dto) {
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(dto, dept);

        if (dto.getParentId() == null || dto.getParentId() == 0) {
            dept.setParentId(0);
            dept.setLevel("0");
        } else {
            SysDept parent = sysDeptMapper.selectById(dto.getParentId());
            Optional.ofNullable(parent)
                    .orElseThrow(() -> new BusinessException("上级部门不存在"));

            int nextSeq = Optional.ofNullable(dto.getSeq()).orElse(1);
            dept.setLevel(parent.getLevel() + "." + nextSeq);
        }
        sysDeptMapper.insert(dept);
    }

    @Override
    public void updateDept(SysDeptDTO dto) {
        Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new BusinessException("部门ID不能为空"));

        SysDept dept = sysDeptMapper.selectById(dto.getId());
        Optional.ofNullable(dept)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        String oldLevel = dept.getLevel();
        Integer originalParentId = dept.getParentId();
        BeanUtils.copyProperties(dto, dept);

        Integer newParentId = dto.getParentId() != null ? dto.getParentId() : originalParentId;

        if (!newParentId.equals(originalParentId)) {
            if (newParentId == 0) {
                dept.setLevel("0");
            } else {
                SysDept parent = sysDeptMapper.selectById(newParentId);
                Optional.ofNullable(parent)
                        .orElseThrow(() -> new BusinessException("上级部门不存在"));
                int nextSeq = Optional.ofNullable(dto.getSeq()).orElse(1);
                dept.setLevel(parent.getLevel() + "." + nextSeq);
            }

            String newLevel = dept.getLevel();
            if (!newLevel.equals(oldLevel)) {
                List<SysDept> children = sysDeptMapper.selectList(
                        new LambdaQueryWrapper<SysDept>().likeRight(SysDept::getLevel, oldLevel + ".")
                );
                children.stream().forEach(child -> {
                    child.setLevel(newLevel + child.getLevel().substring(oldLevel.length()));
                    sysDeptMapper.updateById(child);
                });
            }
        }
        sysDeptMapper.updateById(dept);
    }

    @Override
    public void deleteDept(Integer id) {
        long childCount = sysDeptMapper.selectCount(
                new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id)
        );
        if (childCount > 0) {
            throw new BusinessException("存在子部门，无法删除");
        }

        long userCount = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, id)
        );
        if (userCount > 0) {
            throw new BusinessException("部门下存在用户，无法删除");
        }

        sysDeptMapper.deleteById(id);
    }

    @Override
    public SysDeptVO getWithChildren(Integer id) {
        SysDept dept = sysDeptMapper.selectById(id);
        Optional.ofNullable(dept)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        List<SysDept> descendants = sysDeptMapper.selectList(
                new LambdaQueryWrapper<SysDept>().likeRight(SysDept::getLevel, dept.getLevel() + ".")
        );

        SysDeptVO rootVO = new SysDeptVO();
        BeanUtils.copyProperties(dept, rootVO);

        List<SysDeptVO> childVOs = descendants.stream()
                .map(d -> {
                    SysDeptVO vo = new SysDeptVO();
                    BeanUtils.copyProperties(d, vo);
                    return vo;
                }).collect(Collectors.toList());

        TreeUtil.buildTree(childVOs, SysDeptVO::getId, SysDeptVO::getParentId, SysDeptVO::setChildren);

        List<SysDeptVO> directChildren = childVOs.stream()
                .filter(vo -> id.equals(vo.getParentId()))
                .collect(Collectors.toList());
        rootVO.setChildren(directChildren);
        return rootVO;
    }
}
