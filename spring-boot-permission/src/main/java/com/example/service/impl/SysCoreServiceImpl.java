package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.entity.SysAcl;
import com.example.entity.SysRoleAcl;
import com.example.entity.SysRoleUser;
import com.example.entity.SysUser;
import com.example.mapper.SysAclMapper;
import com.example.mapper.SysRoleAclMapper;
import com.example.mapper.SysRoleUserMapper;
import com.example.service.SysCoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2023/10/23
 * @Classname SysCoreServiceImpl
 * @since 1.0.0
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public List<SysAcl> getUserAclList(Integer userId) {
        if (isSuperAdmin()) {
            return sysAclMapper.selectList(null);
        }

        List<Integer> roleIds = getRoleIdListByUserId(userId);
        return getAclIdListByRoleIdList(roleIds);
    }

    private List<SysAcl> getAclIdListByRoleIdList(List<Integer> roleIds) {
        final List<SysRoleAcl> sysRoleAcls = sysRoleAclMapper.selectList(new LambdaQueryWrapper<SysRoleAcl>().in(SysRoleAcl::getRoleId, roleIds));
        if (CollectionUtils.isEmpty(sysRoleAcls)) {
            return new ArrayList<>();
        }
        List<Integer> aclIdList = sysRoleAcls.stream().map(SysRoleAcl::getAclId).collect(Collectors.toList());
        List<SysAcl> sysAclList = sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().eq(SysAcl::getId, aclIdList));
        return sysAclList;
    }

    private List<Integer> getRoleIdListByUserId(Integer userId) {
        final List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.selectList(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, userId));
        if (CollectionUtils.isEmpty(sysRoleUserList)) {
            return new ArrayList<>();
        }

        return sysRoleUserList.stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
    }

    private boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
//        SysUser sysUser = RequestHolder.getCurrentUser();
        SysUser sysUser = new SysUser();
        sysUser.setMail("admin");
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }
}
