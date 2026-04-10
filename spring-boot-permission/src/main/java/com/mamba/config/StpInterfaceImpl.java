package com.mamba.config;

import cn.dev33.satoken.stp.StpInterface;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.entity.SysRole;
import com.mamba.system.entity.SysRoleAcl;
import com.mamba.system.entity.SysRoleUser;
import com.mamba.system.mapper.SysAclMapper;
import com.mamba.system.mapper.SysRoleAclMapper;
import com.mamba.system.mapper.SysRoleMapper;
import com.mamba.system.mapper.SysRoleUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sa-Token 权限数据加载接口实现
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private SysRoleUserMapper roleUserMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysRoleAclMapper roleAclMapper;
    @Resource
    private SysAclMapper aclMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 获取用户角色 -> 获取角色权限ID -> 获取权限code列表
        List<Integer> roleIds = roleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, loginId)
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> aclIds = roleAclMapper.selectList(
                new LambdaQueryWrapper<SysRoleAcl>().in(SysRoleAcl::getRoleId, roleIds)
        ).stream().map(SysRoleAcl::getAclId).distinct().collect(Collectors.toList());

        if (aclIds.isEmpty()) {
            return new ArrayList<>();
        }

        return aclMapper.selectList(
                new LambdaQueryWrapper<SysAcl>().in(SysAcl::getId, aclIds).eq(SysAcl::getStatus, 1)
        ).stream().map(SysAcl::getCode).filter(code -> code != null && !code.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<Integer> roleIds = roleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, loginId)
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        return roleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roleIds).eq(SysRole::getStatus, 1)
        ).stream().map(SysRole::getName).collect(Collectors.toList());
    }
}
