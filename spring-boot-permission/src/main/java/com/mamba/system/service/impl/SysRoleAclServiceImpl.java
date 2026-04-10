package com.mamba.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.system.dto.RoleAclDTO;
import com.mamba.system.entity.SysRoleAcl;
import com.mamba.system.mapper.SysRoleAclMapper;
import com.mamba.system.service.SysLogService;
import com.mamba.system.service.SysRoleAclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleAclServiceImpl extends ServiceImpl<SysRoleAclMapper, SysRoleAcl> implements SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public List<Integer> getAclIdsByRoleId(Integer roleId) {
        return sysRoleAclMapper.selectList(
                new LambdaQueryWrapper<SysRoleAcl>().eq(SysRoleAcl::getRoleId, roleId)
        ).stream().map(SysRoleAcl::getAclId).collect(Collectors.toList());
    }

    @Override
    public void assignAcls(RoleAclDTO dto) {
        List<Integer> oldAclIds = getAclIdsByRoleId(dto.getRoleId());

        sysRoleAclMapper.delete(
                new LambdaQueryWrapper<SysRoleAcl>().eq(SysRoleAcl::getRoleId, dto.getRoleId())
        );

        dto.getAclIds().stream().forEach(aclId -> {
            SysRoleAcl roleAcl = new SysRoleAcl();
            roleAcl.setRoleId(dto.getRoleId());
            roleAcl.setAclId(aclId);
            sysRoleAclMapper.insert(roleAcl);
        });

        sysLogService.saveLog(7, dto.getRoleId(),
                JSON.toJSONString(oldAclIds), JSON.toJSONString(dto.getAclIds()));
    }
}
