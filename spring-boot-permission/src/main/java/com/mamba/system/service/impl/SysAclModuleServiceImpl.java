package com.mamba.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.util.TreeUtil;
import com.mamba.system.dto.SysAclModuleDTO;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.entity.SysAclModule;
import com.mamba.system.mapper.SysAclMapper;
import com.mamba.system.mapper.SysAclModuleMapper;
import com.mamba.system.service.SysAclModuleService;
import com.mamba.system.vo.SysAclModuleVO;
import com.mamba.system.vo.SysAclVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysAclModuleServiceImpl extends ServiceImpl<SysAclModuleMapper, SysAclModule> implements SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public List<SysAclModuleVO> treeWithAcls() {
        List<SysAclModule> modules = sysAclModuleMapper.selectList(new LambdaQueryWrapper<>());
        List<SysAcl> acls = sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().orderByAsc(SysAcl::getSeq));

        Map<Integer, List<SysAclVO>> aclMap = acls.stream()
                .map(acl -> {
                    SysAclVO vo = new SysAclVO();
                    BeanUtils.copyProperties(acl, vo);
                    vo.setTypeDesc(getTypeDesc(acl.getType()));
                    vo.setStatusDesc(Integer.valueOf(1).equals(acl.getStatus()) ? "正常" : "冻结");
                    return vo;
                })
                .collect(Collectors.groupingBy(SysAclVO::getAclModuleId));

        List<SysAclModuleVO> voList = modules.stream()
                .map(module -> {
                    SysAclModuleVO vo = new SysAclModuleVO();
                    BeanUtils.copyProperties(module, vo);
                    vo.setAclList(aclMap.getOrDefault(module.getId(), new ArrayList<>()));
                    return vo;
                }).collect(Collectors.toList());

        return TreeUtil.buildTree(voList, SysAclModuleVO::getId, SysAclModuleVO::getParentId, SysAclModuleVO::setChildren);
    }

    @Override
    public void saveModule(SysAclModuleDTO dto) {
        SysAclModule module = new SysAclModule();
        BeanUtils.copyProperties(dto, module);

        if (dto.getParentId() == null || dto.getParentId() == 0) {
            module.setParentId(0);
            module.setLevel("0");
        } else {
            SysAclModule parent = sysAclModuleMapper.selectById(dto.getParentId());
            Optional.ofNullable(parent)
                    .orElseThrow(() -> new BusinessException("上级权限模块不存在"));

            int nextSeq = Optional.ofNullable(dto.getSeq()).orElse(1);
            module.setLevel(parent.getLevel() + "." + nextSeq);
        }
        module.setStatus(Optional.ofNullable(dto.getStatus()).orElse(1));
        sysAclModuleMapper.insert(module);
    }

    @Override
    public void updateModule(SysAclModuleDTO dto) {
        Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new BusinessException("权限模块ID不能为空"));

        SysAclModule module = sysAclModuleMapper.selectById(dto.getId());
        Optional.ofNullable(module)
                .orElseThrow(() -> new BusinessException("权限模块不存在"));

        String oldLevel = module.getLevel();
        Integer originalParentId = module.getParentId();
        BeanUtils.copyProperties(dto, module);

        Integer newParentId = dto.getParentId() != null ? dto.getParentId() : originalParentId;

        if (!newParentId.equals(originalParentId)) {
            if (newParentId == 0) {
                module.setLevel("0");
            } else {
                SysAclModule parent = sysAclModuleMapper.selectById(newParentId);
                Optional.ofNullable(parent)
                        .orElseThrow(() -> new BusinessException("上级权限模块不存在"));
                int nextSeq = Optional.ofNullable(dto.getSeq()).orElse(1);
                module.setLevel(parent.getLevel() + "." + nextSeq);
            }

            String newLevel = module.getLevel();
            if (!newLevel.equals(oldLevel)) {
                List<SysAclModule> children = sysAclModuleMapper.selectList(
                        new LambdaQueryWrapper<SysAclModule>().likeRight(SysAclModule::getLevel, oldLevel + ".")
                );
                children.stream().forEach(child -> {
                    child.setLevel(newLevel + child.getLevel().substring(oldLevel.length()));
                    sysAclModuleMapper.updateById(child);
                });
            }
        }
        sysAclModuleMapper.updateById(module);
    }

    @Override
    public void changeStatus(Integer id, Integer status) {
        SysAclModule module = sysAclModuleMapper.selectById(id);
        Optional.ofNullable(module)
                .orElseThrow(() -> new BusinessException("权限模块不存在"));
        module.setStatus(status);
        sysAclModuleMapper.updateById(module);
    }

    @Override
    public void deleteModule(Integer id) {
        long childCount = sysAclModuleMapper.selectCount(
                new LambdaQueryWrapper<SysAclModule>().eq(SysAclModule::getParentId, id)
        );
        if (childCount > 0) {
            throw new BusinessException("存在子权限模块，无法删除");
        }

        long aclCount = sysAclMapper.selectCount(
                new LambdaQueryWrapper<SysAcl>().eq(SysAcl::getAclModuleId, id)
        );
        if (aclCount > 0) {
            throw new BusinessException("权限模块下存在权限，无法删除");
        }

        sysAclModuleMapper.deleteById(id);
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
