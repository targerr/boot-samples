package com.example.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.context.ReqInfoContext;
import com.example.dto.BaseUserInfoDTO;
import com.example.entity.SysAcl;
import com.example.entity.SysRoleAcl;
import com.example.entity.SysRoleUser;
import com.example.entity.SysUser;
import com.example.mapper.SysAclMapper;
import com.example.mapper.SysRoleAclMapper;
import com.example.mapper.SysRoleUserMapper;
import com.example.service.SysCoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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


    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        final List<Integer> aclIds =  sysRoleAclMapper.selectList(new LambdaQueryWrapper<SysRoleAcl>()
                .eq(SysRoleAcl::getRoleId, roleId))
                .stream()
                .map(SysRoleAcl::getAclId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(aclIds)) {
            return new ArrayList<>();
        }

        return sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>()
                .in(SysAcl::getId, aclIds));
    }

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        int userId = Convert.toInt(ReqInfoContext.getReqInfo().getUserId());
        return getUserAclList(userId);
    }

    private List<SysAcl> getAclIdListByRoleIdList(List<Integer> roleIds) {
        final List<SysRoleAcl> sysRoleAcls = sysRoleAclMapper.selectList(new LambdaQueryWrapper<SysRoleAcl>().in(SysRoleAcl::getRoleId, roleIds));
        if (CollectionUtils.isEmpty(sysRoleAcls)) {
            return new ArrayList<>();
        }
        List<Integer> aclIdList = sysRoleAcls.stream().map(SysRoleAcl::getAclId).collect(Collectors.toList());
        return sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().in(SysAcl::getId, aclIdList));
    }

    private List<Integer> getRoleIdListByUserId(Integer userId) {
        final List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.selectList(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, userId));
        if (CollectionUtils.isEmpty(sysRoleUserList)) {
            return new ArrayList<>();
        }

        return sysRoleUserList.stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
    }

    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }

        List<SysAcl> sysAclList = sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().eq(SysAcl::getUrl, url));
        if (CollectionUtils.isEmpty(sysAclList)) {
            return true;
        }

        return sysAclList.stream()
                // 过滤无效权限点
                .filter(acl -> acl != null && acl.getStatus() == 1)
                .anyMatch(acl -> getCurrentUserAclList().stream()
                        .map(SysAcl::getId)
                        .collect(Collectors.toSet())
                        .contains(acl.getId())
                );
    }

    public boolean hasUrlAcl1(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> sysAclList = sysAclMapper.selectList(new LambdaQueryWrapper<SysAcl>().eq(SysAcl::getUrl, url));
        if (CollectionUtils.isEmpty(sysAclList)) {
            return true;
        }
        List<SysAcl> userAclList = getCurrentUserAclList();
        Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());


        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : sysAclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }

        if (!hasValidAcl) {
            return true;
        }
        return false;

    }

    private boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        final BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        if (StringUtils.equalsAnyIgnoreCase("Admin",user.getUserName())) {
            return true;
        }
        return false;
    }
}
